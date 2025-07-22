package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.Like;
import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(Post post, User user);

    Like findByPostAndUser(Post post, User user);
}
