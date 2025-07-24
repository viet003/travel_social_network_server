package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dtos.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.comment.CommentResponse;
import api.v1.travel_social_network_server.reponses.post.PostResponse;
import api.v1.travel_social_network_server.services.PostService;
import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-url}")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{userId}")
    public ResponseEntity<Response<?>> getPostsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            PageableResponse<?> postPage = null;
            if (currentUser != null && currentUser.getUserId().equals(userId)) {
                postPage = postService.getPostsByUser(currentUser, page, size);
            } else {
                User user = new User();
                user.setUserId(userId);
                postPage = postService.getPostsByStatusAndUser(PrivacyEnum.PUBLIC, user, page, size);
            }

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(postPage)
                            .message("Search post successfully.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/post")
    public ResponseEntity<Response<?>> getPostsByStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal User user
    ) {
        try {
            PageableResponse<?> postPage = postService.getPostsByStatus(user, PrivacyEnum.PUBLIC, page, size);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(postPage)
                            .message("Search post successfully.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/post/group/{groupId}")
    public ResponseEntity<Response<?>> getPostsByGroup(
            @PathVariable UUID groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal User user
    ) {
        try {
            PageableResponse<?> postPage = postService.getPostsByGroup(groupId, user, page, size);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(postPage)
                            .message("Search post successfully.")
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(PageableResponse.builder()
                                    .content(Collections.emptyList())
                                    .totalElements(0)
                                    .totalPages(0)
                                    .build())
                            .message("No posts found.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> createPost(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdatePostDto updatePostDto
    ) throws IOException {
        try {
            PostResponse post = postService.createPostMultiTask(user, updatePostDto, null);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(post)
                            .message("Post successfully created.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "/post/group/{groupId}", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> createPostOnGroup(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdatePostDto updatePostDto
    ) throws IOException {
        try {
            PostResponse post = postService.createPostMultiTask(user, updatePostDto, groupId);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(post)
                            .message("Post successfully created.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }
}
