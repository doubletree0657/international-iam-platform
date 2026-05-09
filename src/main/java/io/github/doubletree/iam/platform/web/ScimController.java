package io.github.doubletree.iam.platform.web;

import io.github.doubletree.iam.platform.application.service.GroupApplicationService;
import io.github.doubletree.iam.platform.application.service.UserApplicationService;
import io.github.doubletree.iam.platform.domain.Group;
import io.github.doubletree.iam.platform.domain.User;
import io.github.doubletree.iam.platform.web.dto.ScimCreateGroupRequest;
import io.github.doubletree.iam.platform.web.dto.ScimCreateUserRequest;
import io.github.doubletree.iam.platform.web.dto.ScimGroupResponse;
import io.github.doubletree.iam.platform.web.dto.ScimUserResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scim/v2")
public class ScimController {

    private final UserApplicationService userApplicationService;
    private final GroupApplicationService groupApplicationService;

    public ScimController(
            UserApplicationService userApplicationService,
            GroupApplicationService groupApplicationService) {
        this.userApplicationService = userApplicationService;
        this.groupApplicationService = groupApplicationService;
    }

    @PostMapping("/Users")
    @ResponseStatus(HttpStatus.CREATED)
    public ScimUserResponse createUser(@Valid @RequestBody ScimCreateUserRequest request) {
        User user = userApplicationService.createUser(request.tenantId(), request.userName(), request.displayName());
        return ScimUserResponse.from(user);
    }

    @GetMapping("/Users/{id}")
    public ScimUserResponse getUser(@PathVariable UUID id) {
        return ScimUserResponse.from(userApplicationService.findUser(id));
    }

    @PostMapping("/Groups")
    @ResponseStatus(HttpStatus.CREATED)
    public ScimGroupResponse createGroup(@Valid @RequestBody ScimCreateGroupRequest request) {
        Group group = groupApplicationService.createGroup(request.tenantId(), request.displayName());
        if (request.members() != null) {
            for (UUID memberId : request.members()) {
                group = groupApplicationService.addUserToGroup(group.getId(), memberId);
            }
        }
        return ScimGroupResponse.from(group);
    }

    @GetMapping("/Groups/{id}")
    public ScimGroupResponse getGroup(@PathVariable UUID id) {
        return ScimGroupResponse.from(groupApplicationService.findGroup(id));
    }
}
