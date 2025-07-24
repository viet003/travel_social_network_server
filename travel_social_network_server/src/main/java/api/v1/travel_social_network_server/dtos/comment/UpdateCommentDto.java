package api.v1.travel_social_network_server.dtos.comment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCommentDto {
    @NotNull
    private Long postId;

    private String content;
}
