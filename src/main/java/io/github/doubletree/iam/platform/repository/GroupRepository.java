package io.github.doubletree.iam.platform.repository;

import io.github.doubletree.iam.platform.domain.Group;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, UUID> {
}
