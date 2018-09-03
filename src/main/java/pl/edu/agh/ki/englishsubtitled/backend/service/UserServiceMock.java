package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.model.UserStatistics;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserStatisticsRepository;

@Service
public class UserServiceMock implements UserService {

    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;

    public UserServiceMock(UserRepository userRepository, UserStatisticsRepository userStatisticsRepository){
        this.userRepository = userRepository;
        this.userStatisticsRepository = userStatisticsRepository;
    }

    @Override
    public User authenticate(String token, boolean required) {
        User user = userRepository.findFirstByFacebookUserId(token);

        if (user == null){
            user = new User(token);
            UserStatistics userStatistics = user.getUserStatistics();
            userStatisticsRepository.saveAndFlush(userStatistics);
            userRepository.saveAndFlush(user);
        }

        return user;
    }

    @Override
    public User authenticateAdmin(String token) {
        return new User("mock");
    }
}
