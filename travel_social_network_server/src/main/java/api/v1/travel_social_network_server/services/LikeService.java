package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.entities.Like;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.like.LikeResponse;
import api.v1.travel_social_network_server.responsitories.LikeRepository;
import api.v1.travel_social_network_server.responsitories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeResponse likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Like liked = likeRepository.findByPostAndUser(post, user);
        boolean likedPost = false;
        
        if (liked != null) {
            likeRepository.delete(liked);
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            post.setLikeCount(Math.max(0, likeCount - 1));
        } else {
            Like like = Like.builder()
                    .user(user)
                    .post(post)
                    .build();
            likeRepository.save(like);

            // Tăng likeCount khi like
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            post.setLikeCount(likeCount + 1);
            likedPost = true;
        }

        Post updatePost = postRepository.save(post);

        return LikeResponse.builder()
                .postId(post.getPostId())
                .likeCount(updatePost.getLikeCount())
                .isLiked(likedPost)
                .build();
    }

}
