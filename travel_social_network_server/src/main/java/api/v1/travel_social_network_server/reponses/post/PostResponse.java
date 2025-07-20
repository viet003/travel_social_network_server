package api.v1.travel_social_network_server.reponses.post;

import api.v1.travel_social_network_server.utilities.PostStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long postId;
    private String content;
    private String location;

    private String avatarImg;
    private String coverImg;
    private UUID userId;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;

    private List<PostMediaResponse> mediaList;

    private List<String> tags;
    private Boolean isShare;
    private PostStatusEnum status;
}
