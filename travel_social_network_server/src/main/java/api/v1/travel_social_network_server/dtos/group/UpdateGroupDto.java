package api.v1.travel_social_network_server.dtos.group;

import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGroupDto {
    private String name;
    private String description;
    private MultipartFile avatar;
    private MultipartFile cover;
    private Boolean privacy;
}
