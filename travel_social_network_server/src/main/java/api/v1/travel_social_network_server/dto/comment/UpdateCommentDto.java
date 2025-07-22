package api.v1.travel_social_network_server.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

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
