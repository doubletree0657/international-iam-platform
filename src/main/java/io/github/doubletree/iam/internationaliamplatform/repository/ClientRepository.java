package io.github.doubletree.iam.internationaliamplatform.repository;

import io.github.doubletree.iam.internationaliamplatform.domain.Client;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
