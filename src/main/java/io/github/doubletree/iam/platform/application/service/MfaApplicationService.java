package io.github.doubletree.iam.platform.application.service;

import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.repository.UserRepository;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MfaApplicationService {

    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int TOTP_TIME_STEP_SECONDS = 30;
    private static final int TOTP_DIGITS = 6;

    private final UserRepository userRepository;
    private final AuditApplicationService auditApplicationService;
    private final SecureRandom secureRandom = new SecureRandom();

    public MfaApplicationService(UserRepository userRepository, AuditApplicationService auditApplicationService) {
        this.userRepository = userRepository;
        this.auditApplicationService = auditApplicationService;
    }

    @Transactional
    public MfaEnrollmentResult enrollTotp(UUID userId) {
        User user = findUser(userId);
        String secret = generateSecret();

        user.setMfaSecret(secret);
        user.setMfaEnabled(true);
        User savedUser = userRepository.save(user);

        auditApplicationService.recordEvent(savedUser.getTenant().getId(), "MFA_ENROLLED", "USER", savedUser.getId());
        return new MfaEnrollmentResult(savedUser.getId(), secret);
    }

    @Transactional
    public boolean verifyTotp(UUID userId, String code) {
        User user = findUser(userId);
        if (!user.isMfaEnabled() || user.getMfaSecret() == null) {
            return false;
        }

        boolean valid = isValidTotpCode(user.getMfaSecret(), code, Instant.now());
        if (valid) {
            auditApplicationService.recordEvent(user.getTenant().getId(), "MFA_VERIFIED", "USER", user.getId());
        }
        return valid;
    }

    @Transactional
    public void disableTotp(UUID userId) {
        User user = findUser(userId);
        user.setMfaEnabled(false);
        user.setMfaSecret(null);
        User savedUser = userRepository.save(user);

        auditApplicationService.recordEvent(savedUser.getTenant().getId(), "MFA_DISABLED", "USER", savedUser.getId());
    }

    String generateTotpCode(String secret, Instant instant) {
        byte[] secretBytes = decodeBase32(secret);
        long counter = instant.getEpochSecond() / TOTP_TIME_STEP_SECONDS;
        byte[] counterBytes = ByteBuffer.allocate(Long.BYTES).putLong(counter).array();

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(secretBytes, "HmacSHA1"));
            byte[] hash = mac.doFinal(counterBytes);
            int offset = hash[hash.length - 1] & 0x0f;
            int binary = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);
            int otp = binary % 1_000_000;
            return String.format(Locale.ROOT, "%0" + TOTP_DIGITS + "d", otp);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to generate TOTP code", exception);
        } finally {
            Arrays.fill(secretBytes, (byte) 0);
        }
    }

    private boolean isValidTotpCode(String secret, String code, Instant instant) {
        return generateTotpCode(secret, instant.minusSeconds(TOTP_TIME_STEP_SECONDS)).equals(code)
                || generateTotpCode(secret, instant).equals(code)
                || generateTotpCode(secret, instant.plusSeconds(TOTP_TIME_STEP_SECONDS)).equals(code);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private String generateSecret() {
        byte[] bytes = new byte[20];
        secureRandom.nextBytes(bytes);
        return encodeBase32(bytes);
    }

    private String encodeBase32(byte[] bytes) {
        StringBuilder encoded = new StringBuilder((bytes.length * 8 + 4) / 5);
        int buffer = 0;
        int bitsLeft = 0;

        for (byte value : bytes) {
            buffer = (buffer << 8) | (value & 0xff);
            bitsLeft += 8;
            while (bitsLeft >= 5) {
                encoded.append(BASE32_ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 0x1f));
                bitsLeft -= 5;
            }
        }

        if (bitsLeft > 0) {
            encoded.append(BASE32_ALPHABET.charAt((buffer << (5 - bitsLeft)) & 0x1f));
        }

        return encoded.toString();
    }

    private byte[] decodeBase32(String secret) {
        String normalizedSecret = secret.replace("=", "").replace(" ", "").toUpperCase(Locale.ROOT);
        ByteBuffer decoded = ByteBuffer.allocate(normalizedSecret.length() * 5 / 8);
        int buffer = 0;
        int bitsLeft = 0;

        for (char character : normalizedSecret.toCharArray()) {
            int value = BASE32_ALPHABET.indexOf(character);
            if (value < 0) {
                throw new IllegalArgumentException("Invalid TOTP secret");
            }
            buffer = (buffer << 5) | value;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                decoded.put((byte) ((buffer >> (bitsLeft - 8)) & 0xff));
                bitsLeft -= 8;
            }
        }

        return Arrays.copyOf(decoded.array(), decoded.position());
    }
}
