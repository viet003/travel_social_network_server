package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.Group;
import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query("SELECT g FROM Group g WHERE g.privacy = :privacy")
    Page<Group> findAllByPrivacy(@Param("privacy") PrivacyEnum privacyEnum, Pageable pageable);

    @Query("SELECT g FROM Group g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Group> findAllByGroupNameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT g FROM Group g WHERE LOWER(g.groupName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND g.privacy = :privacy")
    Page<Group> findAllByGroupNameContainingAndPrivacy(@Param("keyword") String keyword, @Param("privacy") PrivacyEnum privacyEnum, Pageable pageable);

    Optional<Group> findByGroupId(UUID groupId);
}