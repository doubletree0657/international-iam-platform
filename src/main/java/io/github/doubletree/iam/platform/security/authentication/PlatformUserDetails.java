package io.github.doubletree.iam.platform.security.authentication;

import io.github.doubletree.iam.platform.domain.AccountStatus;
import io.github.doubletree.iam.platform.domain.PasswordCredential;
import io.github.doubletree.iam.platform.domain.User;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record PlatformUserDetails(
        UUID userId,
        UUID tenantId,
        String username,
        String password,  
        AccountStatus accountStatus)
        implements UserDetails {

    public static PlatformUserDetails from(User user) {
        PasswordCredential credential = user.getPasswordCredential();
        return new PlatformUserDetails(
                user.getId(),
                user.getTenant().getId(),
                user.getUsername(),
                credential == null ? null : credential.getPasswordHash(),
                user.getAccountStatus());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus != AccountStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "PlatformUserDetails[userId=%s, tenantId=%s, username=%s, accountStatus=%s]"
                .formatted(userId, tenantId, username, accountStatus);
    }
}
