package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dtos.group.UpdateGroupDto;
import api.v1.travel_social_network_server.dtos.user.UpdateUserImgDto;
import api.v1.travel_social_network_server.entities.Group;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.comment.CommentResponse;
import api.v1.travel_social_network_server.reponses.group.GroupResponse;
import api.v1.travel_social_network_server.reponses.user.UserResponse;
import api.v1.travel_social_network_server.services.GroupService;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-url}")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<Response<?>> getGroupById(@PathVariable("groupId") UUID groupId, @AuthenticationPrincipal User user) {
        try {
            GroupResponse group = groupService.getGroupById(groupId, user);

            return ResponseEntity.ok(Response.builder()
                    .status(StatusRequestEnum.SUCCESS)
                    .data(group)
                    .message("Successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/group")
    public ResponseEntity<Response<?>> getAllGroupsByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String privacy,
            @AuthenticationPrincipal User user
    ) {
        try {
            PageableResponse<?> groups = groupService.getAllGroupsByName(user, keyword, privacy, page, size);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(groups)
                            .message("Search group successfully.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "/group", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> createGroup(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdateGroupDto updateGroupDto
    ) throws IOException {
        try {
            GroupResponse group = groupService.createGroup(user, updateGroupDto);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(group)
                            .message("Create successfully")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<UserResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

}
