package api.v1.travel_social_network_server.reponses.auth;

import api.v1.travel_social_network_server.entities.UserProfile;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private UUID userId;
    private String userName;
    private String email;
    private String token;
    private String avatarImg;
    private String coverImg;
    private String role;
    private UserProfile userProfile;
}
