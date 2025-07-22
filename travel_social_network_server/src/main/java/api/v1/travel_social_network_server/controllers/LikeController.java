package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.comment.CommentResponse;
import api.v1.travel_social_network_server.reponses.like.LikeResponse;
import api.v1.travel_social_network_server.services.LikeService;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-url}")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/like/{postId}")
    ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        try {
            LikeResponse liked = likeService.likePost(postId, user);

            return ResponseEntity.ok(Response.builder()
                    .status(StatusRequestEnum.SUCCESS)
                    .data(liked)
                    .message("Updated Like successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }
}
