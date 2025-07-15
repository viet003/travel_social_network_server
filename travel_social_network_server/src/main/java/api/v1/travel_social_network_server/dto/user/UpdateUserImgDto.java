package api.v1.travel_social_network_server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserImgDto {
    private MultipartFile coverImg;
    private MultipartFile avatarImg;
}
