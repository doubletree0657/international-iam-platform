package io.github.doubletree.iam.platform.repository;

import io.github.doubletree.iam.platform.domain.Client;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
