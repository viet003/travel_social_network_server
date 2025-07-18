package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dto.post.UpdatePostDto;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.PostMedia;
import api.v1.travel_social_network_server.entities.Tag;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.responsitories.PostRepository;
import api.v1.travel_social_network_server.utilities.PostStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String POST_FOLDER = "posts";

    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;

    public Page<Post> getPostsByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllByUser(user, pageable);
    }

    @Transactional
    public boolean createPost(User user, UpdatePostDto dto) {

        Post post = Post.builder()
                .user(user)
                .content(dto.getContent())
                .location(dto.getLocation())
                .status(PostStatusEnum.PUBLIC)
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

            for (PostMedia media : mediaList) {
                media.setPost(post);
            }

            post.setMediaList(mediaList);
        }

        postRepository.save(post);

        return true;
    }
}
