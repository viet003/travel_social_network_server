package api.v1.travel_social_network_server.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDto {
    private String content;
    private String location;
    private List<MultipartFile> media;
    private String mediaType;
    private List<String> tags;
}
