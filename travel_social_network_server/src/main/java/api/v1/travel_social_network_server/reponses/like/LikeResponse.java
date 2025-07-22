package api.v1.travel_social_network_server.reponses.like;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    private Long postId;
    private Integer likeCount;
    private boolean isLiked;
}
