package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dtos.comment.UpdateCommentDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.comment.CommentResponse;
import api.v1.travel_social_network_server.services.CommentService;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.base-url}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<Response<?>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            PageableResponse<?> postPage = commentService.getCommentsByPost(postId, page, size);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(postPage)
                            .message("Search comment successfully.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<CommentResponse>builder()
                    .status(StatusRequestEnum.FAIL)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping()
    public ResponseEntity<Response<?>> createComment(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateCommentDto updateCommentDto
    ) throws IOException {
        try {
            CommentResponse comment = commentService.createComment(user, updateCommentDto);

            return ResponseEntity.ok(
                    Response.builder()
                            .status(StatusRequestEnum.SUCCESS)
                            .data(comment)
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
