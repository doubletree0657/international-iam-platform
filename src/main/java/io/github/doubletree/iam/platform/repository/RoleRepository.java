package io.github.doubletree.iam.platform.repository;

import io.github.doubletree.iam.platform.domain.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
