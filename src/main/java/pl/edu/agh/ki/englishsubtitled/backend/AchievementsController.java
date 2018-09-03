package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.model.UserStatistics;
import pl.edu.agh.ki.englishsubtitled.backend.service.UserService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/achievements")
public class AchievementsController {

    private final UserService userService;

    @Autowired
    AchievementsController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AchievementDto> getAchievements(@RequestHeader("Authorization") String token){
        User user = userService.authenticate(token, true);
        UserStatistics userStatistics = user.getUserStatistics();
        return userStatistics.getRecognizedAchievementsDtos();
    }
}
