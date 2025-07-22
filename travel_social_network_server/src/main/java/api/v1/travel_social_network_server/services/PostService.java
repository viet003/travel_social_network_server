package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dto.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.*;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.post.PostMediaResponse;
import api.v1.travel_social_network_server.reponses.post.PostResponse;
import api.v1.travel_social_network_server.responsitories.PostRepository;
import api.v1.travel_social_network_server.utilities.PostStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String POST_FOLDER = "posts";

    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;

    public PageableResponse<PostResponse> getPostsByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByUser(user, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(this::convertToPostResponse)
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PageableResponse<PostResponse> getPostsByStatus(PostStatusEnum status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByStatus(status, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(this::convertToPostResponse)
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PageableResponse<PostResponse> getPostsByStatusAndUser(PostStatusEnum status, User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByStatusAndUser(status, user, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(this::convertToPostResponse)
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    @Transactional
    public Post createPost(User user, UpdatePostDto dto) {

        Post post = Post.builder()
                .user(user)
                .content(dto.getContent())
                .location(dto.getLocation())
                .status(PostStatusEnum.PUBLIC)
                .isShare(false)
                .build();

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<Tag> tagList = dto.getTags().stream()
                    .map(d -> Tag.builder()
                            .title(d)
                            .post(post)
                            .build())
                    .toList();
            post.setTags(tagList);
        }

        if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
            List<PostMedia> mediaList = cloudinaryService.uploadMultipleFiles(dto.getMedia(), POST_FOLDER);
            mediaList.forEach(media -> media.setPost(post));
            post.setMediaList(mediaList);
        }

        return postRepository.save(post);
    }



    private PostResponse convertToPostResponse(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .location(post.getLocation())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .shareCount(post.getShareCount())

                .userId(post.getUser().getUserId())
                .avatarImg(post.getUser().getAvatarImg())
                .coverImg(post.getUser().getCoverImg())
                .firstName(post.getUser().getUserProfile().getFirstName())
                .lastName(post.getUser().getUserProfile().getLastName())

                .mediaList(post.getMediaList() != null ? post.getMediaList().stream()
                        .map(media -> PostMediaResponse.builder()
                                .mediaId(media.getMediaId())
                                .url(media.getUrl())
                                .type(media.getType())
                                .build())
                        .collect(Collectors.toList()) : List.of())

                .tags(post.getTags() != null
                        ? post.getTags().stream().map(Tag::getTitle).collect(Collectors.toList())
                        : List.of())

                .isShare(post.getIsShare() != null ? post.getIsShare() : false)

                .status(post.getStatus())

                .build();
    }
}
