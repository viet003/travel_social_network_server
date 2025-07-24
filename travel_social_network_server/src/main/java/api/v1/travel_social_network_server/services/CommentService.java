package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dtos.comment.UpdateCommentDto;
import api.v1.travel_social_network_server.entities.Comment;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.comment.CommentResponse;
import api.v1.travel_social_network_server.responsitories.CommentRepository;
import api.v1.travel_social_network_server.responsitories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PageableResponse<CommentResponse> getCommentsByPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Optional<Post> post = postRepository.findById(postId);
        Page<Comment> comments = commentRepository.findAllByPost(post.get(), pageable);

        List<CommentResponse> content = comments.getContent().stream()
                .map(this::convertToCommentResponse)
                .toList();

        return PageableResponse.<CommentResponse>builder()
                .content(content)
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();
    }


    @Transactional
    public CommentResponse createComment(User user, UpdateCommentDto updateCommentDto) {
        System.out.println(updateCommentDto);

        Post post = postRepository.findById(updateCommentDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = Comment.builder()
                .user(user)
                .content(updateCommentDto.getContent())
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return CommentResponse.builder()
                .firstName(savedComment.getUser().getUserProfile().getFirstName())
                .lastName(savedComment.getUser().getUserProfile().getLastName())
                .avatarImg(savedComment.getUser().getAvatarImg())
                .content(savedComment.getContent())
                .createdAt(savedComment.getCreatedAt())
                .build();
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .firstName(comment.getUser().getUserProfile().getFirstName())
                .lastName(comment.getUser().getUserProfile().getLastName())
                .avatarImg(comment.getUser().getAvatarImg())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
