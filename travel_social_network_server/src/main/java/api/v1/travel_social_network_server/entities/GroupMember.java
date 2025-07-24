package api.v1.travel_social_network_server.entities;

import api.v1.travel_social_network_server.utilities.GroupRoleEnum;
import api.v1.travel_social_network_server.utilities.RoleEnum;
import api.v1.travel_social_network_server.utilities.StatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @Column(name = "group_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GroupIdMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonBackReference
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private GroupRoleEnum role;

    @PrePersist
    public void prePersist() {
        if (status == null) status = StatusEnum.ACTIVE;
    }
}
