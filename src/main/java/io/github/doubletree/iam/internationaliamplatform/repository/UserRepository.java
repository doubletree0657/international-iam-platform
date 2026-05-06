package io.github.doubletree.iam.internationaliamplatform.repository;

import io.github.doubletree.iam.internationaliamplatform.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
}
