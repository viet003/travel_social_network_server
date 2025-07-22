package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.Comment;
import api.v1.travel_social_network_server.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);
}
