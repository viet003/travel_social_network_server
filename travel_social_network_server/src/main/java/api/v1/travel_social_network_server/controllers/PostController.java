package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dto.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.services.PostService;
import api.v1.travel_social_network_server.utilities.PostStatusEnum;
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
public class PostController {

    private final PostService postService;

    @GetMapping("/post/{userId}")
    public ResponseEntity<Response<?>> getPostsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        PageableResponse<?> postPage = null;
        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            postPage = postService.getPostsByUser(currentUser, page, size);
        } else {
            User user = new User();
            user.setUserId(userId);
            postPage = postService.getPostsByStatusAndUser(PostStatusEnum.PUBLIC, user, page, size);
        }

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .data(postPage)
                        .message("Search post successfully.")
                        .build()
        );
    }

    @GetMapping("/post")
    public ResponseEntity<Response<?>> getPostsByStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .data(postService.getPostsByStatus(PostStatusEnum.PUBLIC, page, size))
                        .message("Search post successfully.")
                        .build()
        );
    }

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> createPost(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdatePostDto updatePostDto
    ) throws IOException {

        Post post = postService.createPost(user, updatePostDto);

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .data(post)
                        .message("Post successfully created.")
                        .build()
        );
    }
}
