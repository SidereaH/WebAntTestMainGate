package webant.maingate.repositories;

import webant.maingate.models.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
//    Optional<User> findByPhone(String phone);
Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
