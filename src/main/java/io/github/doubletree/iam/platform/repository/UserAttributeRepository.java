package io.github.doubletree.iam.platform.repository;

import io.github.doubletree.iam.platform.domain.UserAttribute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAttributeRepository extends JpaRepository<UserAttribute, UUID> {
}
