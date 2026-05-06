package io.github.doubletree.iam.internationaliamplatform.repository;

import io.github.doubletree.iam.internationaliamplatform.domain.Permission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}
