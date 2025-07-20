package api.v1.travel_social_network_server.reponses.post;

import api.v1.travel_social_network_server.utilities.MediaTypeEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMediaResponse {
    private Long mediaId;
    private String url;
    private MediaTypeEnum type;
}