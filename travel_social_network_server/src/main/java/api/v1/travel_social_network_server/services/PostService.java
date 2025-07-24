package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dtos.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.*;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.group.GroupResponse;
import api.v1.travel_social_network_server.reponses.post.PostMediaResponse;
import api.v1.travel_social_network_server.reponses.post.PostResponse;
import api.v1.travel_social_network_server.responsitories.GroupMemberRepository;
import api.v1.travel_social_network_server.responsitories.GroupRepository;
import api.v1.travel_social_network_server.responsitories.PostRepository;
import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String POST_FOLDER = "posts";

    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public PageableResponse<PostResponse> getPostsByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByUser(user, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, user))
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PageableResponse<PostResponse> getPostsByGroup(UUID groupId, User user, int page, int size) {

        if(!groupMemberRepository.existsByGroupGroupIdAndUserUserId(groupId, user.getUserId()) ) {
            throw new ResourceNotFoundException("User not member of group with id: " + groupId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByGroupGroupId(groupId, pageable);
        System.out.println(posts.getContent());

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, user))
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PageableResponse<PostResponse> getPostsByStatus(User user, PrivacyEnum privacy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByPrivacy(privacy, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, user))
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    public PageableResponse<PostResponse> getPostByGroup(UUID groupId,User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByGroupGroupId(groupId, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, user))
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }


    public PageableResponse<PostResponse> getPostsByStatusAndUser(PrivacyEnum privacy, User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByPrivacyAndUser(privacy, user, pageable);

        List<PostResponse> content = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, user))
                .toList();

        return PageableResponse.<PostResponse>builder()
                .content(content)
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
    }

    @Transactional
    public PostResponse createPostMultiTask(User user, UpdatePostDto dto, UUID groupId) {

        Post post = Post.builder()
                .user(user)
                .content(dto.getContent())
                .location(dto.getLocation())
                .privacy(PrivacyEnum.PUBLIC)
                .isShare(false)
                .build();

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<Tag> tagList = dto.getTags().stream()
                    .map(tagTitle -> Tag.builder()
                            .title(tagTitle)
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

        if (groupId != null) {
            Group group = groupRepository.findByGroupId(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
            post.setGroup(group);
        }

        Post e = postRepository.save(post);

        return convertToPostResponse(e, user);
    }


    private PostResponse convertToPostResponse(Post post, User user) {
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
                .privacy(post.getPrivacy())
                .group(post.getGroup() != null ? GroupResponse.builder()
                        .groupId(post.getGroup().getGroupId())
                        .groupName(post.getGroup().getGroupName())
                        .coverImageUrl(post.getGroup().getCoverImageUrl())
                        .build() : null)
                .isLiked(user != null && post.getLikes() != null && post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getUserId().equals(user.getUserId())))
                .build();
    }
}
