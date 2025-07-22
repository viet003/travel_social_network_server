package api.v1.travel_social_network_server.dto.auth;

import api.v1.travel_social_network_server.utilities.GenderEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegisterDto {

    @NotNull
    private String userName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
}
