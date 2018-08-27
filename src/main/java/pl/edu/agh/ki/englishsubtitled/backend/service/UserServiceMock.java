package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;

@Service
public class UserServiceMock implements UserService {

    private final UserRepository userRepository;

    public UserServiceMock(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User authenticate(String token, boolean required) {
        User user = userRepository.findFirstByFacebookUserId(token);

        if (user == null){
            user = new User(token);
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public User authenticateAdmin(String token) {
        return new User("mock");
    }
}
