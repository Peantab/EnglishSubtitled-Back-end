package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;

@Service
public class UserServiceMock implements UserService {

    public UserServiceMock(){}

    public User authenticate(String token, boolean required) {
        return new User("mock");
    }

    @Override
    public User authenticateAdmin(String token) {
        return new User("mock");
    }
}
