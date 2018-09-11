package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.UserDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    UsersController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserDto getInformation(@RequestHeader("Authorization") String token){
        User user = userService.authenticate(token, true);
        return user.getDto();
    }
}
