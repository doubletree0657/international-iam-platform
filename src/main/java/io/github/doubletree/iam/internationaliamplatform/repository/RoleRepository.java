package io.github.doubletree.iam.internationaliamplatform.repository;

import io.github.doubletree.iam.internationaliamplatform.domain.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
