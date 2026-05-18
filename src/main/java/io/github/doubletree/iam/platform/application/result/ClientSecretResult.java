package io.github.doubletree.iam.platform.application.result;

import io.github.doubletree.iam.platform.domain.Client;

public record ClientSecretResult(Client client, String clientSecret) {
}
