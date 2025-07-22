package api.v1.travel_social_network_server.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    @NotNull
    private String email;

    @NotNull
    private String password;
    private String role;
}
