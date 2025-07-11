package api.v1.travel_social_network_server.responsitories;

import api.v1.travel_social_network_server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

//    Page<User> findAll(Pageable pageable);
//
//    boolean existsByUsername(String username);
//
    boolean existsByEmail(String email);
//
//    List<User> findByUsernameIn(Set<String> usernames);
//
//
//    Page<User> findAllByRole(RoleEnum roleEnum, Pageable pageable);
//
//    //    @Query("SELECT u FROM User u WHERE u.role <> :roleEnum")
//    Page<User> findAllByRoleNot(RoleEnum roleEnum, Pageable of);

    Optional<User> findByEmail(String email);
}