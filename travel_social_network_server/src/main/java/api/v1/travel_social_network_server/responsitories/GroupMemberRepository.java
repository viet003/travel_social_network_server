package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupGroupIdAndUserUserId(UUID groupId, UUID userId);
}
