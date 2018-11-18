package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonResultsDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AchievementsControllerTests {

    @Autowired
    AchievementsController achievementsController;

    @Autowired
    LessonsController lessonsController;

    @Autowired
    ProgressController progressController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    LessonRepository lessonRepository;

    @LocalServerPort
    private int port;

    private String username;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        username = "fooUser";
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    public void newUserHasNoAchievements() {
        List<AchievementDto> achievementDtos = achievementsController.getAchievements(username);
        assertEquals(0, achievementDtos.size());
    }

    @Test
    @Transactional
    public void achievingFirstBloodAchievement() {
        // Prepare environment for getting an achievement
        Film film = new Film("Film");
        filmRepository.saveAndFlush(film);
        Lesson lesson1 = new Lesson("Lesson1", film, Collections.emptyList());
        lessonRepository.saveAndFlush(lesson1);
        int lesson1Id = lesson1.getLessonId();

        // Fulfill requirements for an achievement
        lessonsController.getLesson(username, String.valueOf(lesson1Id));
        List<AchievementDto> achievementDelta = progressController.updateLessonState(username, String.valueOf(lesson1Id), new LessonResultsDto());

        // Get all user achievements.
        List<AchievementDto> userAchievements = achievementsController.getAchievements(username);

        assertTrue("Newly received achievements are a subset of all User's achievements", userAchievements.containsAll(achievementDelta));

        boolean receivedFirstBloodAchievement = doesAchievementDeltaContainsAchievement(achievementDelta, AchievementEntry.Achievement.FIRST_BLOOD);

        assertTrue("User should get First Blood achievement for finishing his first lesson.", receivedFirstBloodAchievement);
    }

    @Test
    @Transactional
    public void achievingTheTempleOfDoomsAchievement() {
        // Prepare environment for getting an achievement
        Film film = new Film("Film");
        filmRepository.saveAndFlush(film);
        Lesson lesson1 = new Lesson("Lesson1", film, Collections.emptyList());
        lessonRepository.saveAndFlush(lesson1);
        int lesson1Id = lesson1.getLessonId();

        // Fulfill requirements for an achievement
        lessonsController.getLesson(username, String.valueOf(lesson1Id));
        LessonResultsDto results = new LessonResultsDto();
        results.mistakes = 83;
        List<AchievementDto> achievementDelta = progressController.updateLessonState(username, String.valueOf(lesson1Id), results);

        boolean receivedTheTempleOfDoomAchievement = doesAchievementDeltaContainsAchievement(achievementDelta, AchievementEntry.Achievement.THE_TEMPLE_OF_DOOM);
        assertFalse("User should not get The Temple of Doom achievement before satisfying conditions.", receivedTheTempleOfDoomAchievement);

        results.mistakes = 1;
        achievementDelta = progressController.updateLessonState(username, String.valueOf(lesson1Id), results);

        receivedTheTempleOfDoomAchievement = doesAchievementDeltaContainsAchievement(achievementDelta, AchievementEntry.Achievement.THE_TEMPLE_OF_DOOM);
        assertTrue("User should get The Temple of Doom achievement as soon as he satisfy conditions.", receivedTheTempleOfDoomAchievement);

        // Get all user achievements.
        List<AchievementDto> userAchievements = achievementsController.getAchievements(username);
        assertTrue("Newly received achievements are a subset of all User's achievements", userAchievements.containsAll(achievementDelta));

        achievementDelta = progressController.updateLessonState(username, String.valueOf(lesson1Id), results);

        receivedTheTempleOfDoomAchievement = doesAchievementDeltaContainsAchievement(achievementDelta, AchievementEntry.Achievement.THE_TEMPLE_OF_DOOM);
        assertFalse("User should get The Temple of Doom achievement only once.", receivedTheTempleOfDoomAchievement);
    }

    private boolean doesAchievementDeltaContainsAchievement(List<AchievementDto> achievementDelta, AchievementEntry.Achievement achievement) {
        for (AchievementDto achievementDto : achievementDelta) {
            if (achievementDto.getAchievement().equals(achievement.toString())) return true;
        }
        return false;
    }

    @Test
    public void endpointTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", username);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Because one cannot simply get for List<>...
        ResponseEntity<List<AchievementDto>> responseEntity =
                restTemplate.exchange(String.format("http://localhost:%d/achievements", port),
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<AchievementDto>>() {
                        });

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
    }
}
