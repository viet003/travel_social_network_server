package api.v1.travel_social_network_server.reponses.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private UUID id;
    private String username;
    private String email;
    private String token;
    private String avatar;
    private String role;
}
