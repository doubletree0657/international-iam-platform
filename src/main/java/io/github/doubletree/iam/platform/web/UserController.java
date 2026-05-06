package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.UserApplicationService;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.web.dto.CreateUserRequest;
import io.github.doubletree.iam.platform.web.dto.UserResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userApplicationService.createUser(request.tenantId(), request.username(), request.displayName());
        return UserResponse.from(user);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public UserResponse assignRoleToUser(@PathVariable UUID userId, @PathVariable UUID roleId) {
        User user = userApplicationService.assignRoleToUser(userId, roleId);
        return UserResponse.from(user);
    }
}
