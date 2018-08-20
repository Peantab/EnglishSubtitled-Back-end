package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.ki.englishsubtitled.backend.dto.FacebookMeResponse;
import pl.edu.agh.ki.englishsubtitled.backend.exception.AdminPrivilegesRequiredException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.AuthenticationException;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User authenticate(String token, boolean required) {
        RestTemplate restTemplate = new RestTemplate();
        FacebookMeResponse facebookMeResponse;
        try {
            facebookMeResponse = restTemplate.getForObject("https://graph.facebook.com/v3.1/me?fields=id,name&access_token={token}", FacebookMeResponse.class, token);
            if(facebookMeResponse == null) throw new NullPointerException();
        } catch(RestClientException|NullPointerException e){
            if (required) throw new AuthenticationException();
            return null;
        }
        return getOrCreateUser(facebookMeResponse.id);
    }

    private User getOrCreateUser(String facebookUserId) {
        User user = userRepository.findFirstByFacebookUserId(facebookUserId);

        if (user == null){
            user = new User(facebookUserId);
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public User authenticateAdmin(String token) {
        User user = authenticate(token, true);
        if(!user.isAdmin()){
            throw new AdminPrivilegesRequiredException();
        }
        return user;
    }
}
