package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAllByGroupGroupId(UUID groupId, Pageable pageable);

    Page<Post> findByPrivacy(PrivacyEnum privacy, Pageable pageable);

    Page<Post> findByPrivacyAndUser(PrivacyEnum privacy, User user, Pageable pageable);
}
