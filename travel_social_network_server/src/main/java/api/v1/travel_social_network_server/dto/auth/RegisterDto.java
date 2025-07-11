package api.v1.travel_social_network_server.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegisterDto {
    private String username;
    private String password;
    private String email;
}
