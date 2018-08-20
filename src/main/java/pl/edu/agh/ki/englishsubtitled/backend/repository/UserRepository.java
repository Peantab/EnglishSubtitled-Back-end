package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByFacebookUserId(String facebookUserId);
}
