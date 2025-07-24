package api.v1.travel_social_network_server.reponses.group;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponse {
    private UUID groupId;
    private String groupName;
    private String groupDescription;
    private String coverImageUrl;
    private Integer memberCount;
    private Boolean privacy;
    private Boolean isMember;
}
