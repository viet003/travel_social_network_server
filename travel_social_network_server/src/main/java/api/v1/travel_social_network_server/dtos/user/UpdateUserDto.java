package api.v1.travel_social_network_server.dtos.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String location;
    private String gender;
    private LocalDate dateOfBirth;
    private String about;
}
