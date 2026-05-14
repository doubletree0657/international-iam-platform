package io.github.doubletree.iam.platform.repository;

import io.github.doubletree.iam.platform.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByUsername(String username);
}
