package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dto.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.services.PostService;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<Post> getPostsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = new User();
        user.setUserId(userId);

        return postService.getPostsByUser(user, page, size);
    }

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> createPost(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdatePostDto updatePostDto
    ) throws IOException {

        postService.createPost(user, updatePostDto);

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .message("Đăng bài viết thành công.")
                        .build()
        );
    }
}
