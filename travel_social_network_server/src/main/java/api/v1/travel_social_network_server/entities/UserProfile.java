package api.v1.travel_social_network_server.entities;

import api.v1.travel_social_network_server.utilities.GenderEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id")
    private Integer userProfileId;

    @Column(name = "first_name", length = 45, columnDefinition = "nvarchar(255)")
    private String firstName;

    @Column(name = "last_name", length = 45, columnDefinition = "nvarchar(255)")
    private String lastName;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "gender", length = 10)
    private GenderEnum gender;

    @Column(name = "dob")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

}
