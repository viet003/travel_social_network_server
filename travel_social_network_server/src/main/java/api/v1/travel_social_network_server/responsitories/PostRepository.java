package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.Post;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.utilities.PostStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Post entities.
 * Provides CRUD operations and custom queries for Post management.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds all posts by a specific user with pagination support
     *
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> findAllByUser(User user, Pageable pageable);

    /**
     * Finds all posts by status with pagination support
     *
     * @param status   the status of posts to retrieve
     * @param pageable pagination information
     * @return page of posts
     */
    Page<Post> findByStatus(PostStatusEnum status, Pageable pageable);

    Page<Post> findByStatusAndUser(PostStatusEnum status, User user, Pageable pageable);
}
