package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonResultsDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.ProgressDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdInvalidException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonNotRentedException;
import pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry;
import pl.edu.agh.ki.englishsubtitled.backend.model.LessonState;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonStateRepository;
import pl.edu.agh.ki.englishsubtitled.backend.service.AchievementService;
import pl.edu.agh.ki.englishsubtitled.backend.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final LessonStateRepository lessonStateRepository;
    private final UserService userService;
    private final AchievementService achievementService;

    @Autowired
    ProgressController(LessonStateRepository lessonStateRepository, UserService userService, AchievementService achievementService){
        this.lessonStateRepository = lessonStateRepository;
        this.userService = userService;
        this.achievementService = achievementService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ProgressDto getProgress(@RequestHeader("Authorization") String token){
        User user = userService.authenticate(token, true);
        return new ProgressDto(user);
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.DELETE)
    public void cancelRental(@RequestHeader("Authorization") String token, @PathVariable String lessonId){
        User user = userService.authenticate(token, true);
        int lessonIdInt = lessonIdToInt(lessonId);
        Optional<LessonState> lessonState = user.getLessonState(lessonIdInt);

        if (!lessonState.isPresent() || lessonState.get().getState() != LessonState.State.RENTED) return;

        user.removeRental(lessonState.get());
        lessonStateRepository.delete(lessonState.get());
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.PUT)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<AchievementDto> updateLessonState(@RequestHeader("Authorization") String token,
                                                  @PathVariable String lessonId,
                                                  @RequestBody LessonResultsDto lessonResults){
        User user = userService.authenticate(token, true);
        int lessonIdInt = lessonIdToInt(lessonId);

        Optional<LessonState> lessonState = user.getLessonState(lessonIdInt);
        if(!lessonState.isPresent()) throw new LessonNotRentedException(lessonIdInt);

        achievementService.acceptLessonResults(user, lessonIdInt, lessonResults);
        List<AchievementEntry> newAchievements = achievementService.recognizeNewAchievements(user);

        lessonState.get().setFinished();
        lessonStateRepository.flush();

        return newAchievements.stream().map(AchievementEntry::getDto).collect(Collectors.toList());
    }

    private int lessonIdToInt(String lessonId){
        int lessonIdInt;
        try {
            lessonIdInt = Integer.parseInt(lessonId);
        } catch(NumberFormatException e){
            throw new LessonIdInvalidException(lessonId);
        }
        return lessonIdInt;
    }
}
