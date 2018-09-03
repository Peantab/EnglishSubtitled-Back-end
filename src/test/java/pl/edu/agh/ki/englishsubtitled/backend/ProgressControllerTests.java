package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonResultsDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.ProgressDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonNotRentedException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonRentalLimitExceededException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgressControllerTests {

    @Autowired
    ProgressController progressController;

    @Autowired
    LessonsController lessonsController;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FilmRepository filmRepository;

    private int lesson1Id, lesson2Id, lesson3Id, lesson4Id;
    private Lesson lesson1, lesson2,lesson3, lesson4;
    private LessonResultsDto lessonResult;

    @Before
    public void setUp(){
        Film film = new Film("Film");
        filmRepository.saveAndFlush(film);
        lesson1 = new Lesson("Lesson1", film, Collections.emptyList());
        lesson2 = new Lesson("Lesson2", film, Collections.emptyList());
        lesson3 = new Lesson("Lesson3", film, Collections.emptyList());
        lesson4 = new Lesson("Lesson4", film, Collections.emptyList());
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);
        lessonRepository.save(lesson4);
        lessonRepository.flush();
        lesson1Id = lesson1.getLessonId();
        lesson2Id = lesson2.getLessonId();
        lesson3Id = lesson3.getLessonId();
        lesson4Id = lesson4.getLessonId();
        lessonResult = new LessonResultsDto();
        userRepository.deleteAll();
    }

    @After
    public void tearDown(){
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @Transactional
    public void rentLesson(){
        lessonsController.getLesson("user1", String.valueOf(lesson1Id));
        ProgressDto progressDto = progressController.getProgress("user1");
        assertEquals(lesson1.getSummary(), progressDto.rented.get(0));

        ProgressDto progressOfAnotherUser = progressController.getProgress("user2");
        assertTrue(progressOfAnotherUser.rented.isEmpty());

    }

    @Test(expected = LessonNotRentedException.class)
    @Transactional
    public void cantFinishNotRentedLesson(){
        progressController.updateLessonState("user1", String.valueOf(lesson1Id), lessonResult);
    }

    @Test
    @Transactional
    public void finishLesson(){
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        progressController.updateLessonState("user1", String.valueOf(lesson2Id), lessonResult);
        ProgressDto progressDto = progressController.getProgress("user1");
        assertEquals(lesson2.getSummary(), progressDto.finished.get(0));
    }

    @Test
    @Transactional
    public void cantRentFinishedLesson(){
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        ProgressDto progressDto = progressController.getProgress("user1");
        assertEquals(lesson2.getSummary(), progressDto.rented.get(0));

        progressController.updateLessonState("user1", String.valueOf(lesson2Id), lessonResult);
        progressDto = progressController.getProgress("user1");
        assertEquals(lesson2.getSummary(), progressDto.finished.get(0));

        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        progressDto = progressController.getProgress("user1");
        assertTrue("Finished lesson cannot appear on the rented list.",progressDto.rented.isEmpty());
    }

    @Test(expected = LessonRentalLimitExceededException.class)
    @Transactional
    public void cantRentMoreLessonsThanLimit(){
        assertTrue("This test assumed that rented books limit is no greater than 3, please update the test.", Configuration.getInstance().getRentedLessonsLimit() < 4);
        lessonsController.getLesson("user1", String.valueOf(lesson1Id));
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        lessonsController.getLesson("user1", String.valueOf(lesson3Id));
        lessonsController.getLesson("user1", String.valueOf(lesson4Id));
    }

    @Test
    @Transactional
    public void cancelLesson(){
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        lessonsController.getLesson("user1", String.valueOf(lesson3Id));
        ProgressDto progressDto = progressController.getProgress("user1");
        assertTrue(progressDto.rented.contains(lesson2.getSummary()));
        assertTrue(progressDto.rented.contains(lesson3.getSummary()));

        progressController.cancelRental("user1", String.valueOf(lesson2Id));
        progressDto = progressController.getProgress("user1");
        assertEquals(lesson3.getSummary(), progressDto.rented.get(0));
        assertEquals(1, progressDto.rented.size());
        assertTrue(progressDto.finished.isEmpty());
    }

    @Test
    @Transactional
    public void cancellingDoesntClearHistory(){
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));

        progressController.updateLessonState("user1", String.valueOf(lesson2Id), lessonResult);
        ProgressDto progressDto = progressController.getProgress("user1");
        assertEquals(lesson2.getSummary(), progressDto.finished.get(0));

        progressController.cancelRental("user1", String.valueOf(lesson2Id));
        progressDto = progressController.getProgress("user1");
        assertEquals(lesson2.getSummary(), progressDto.finished.get(0));
    }

    @Test
    @Transactional
    public void twoUserScenario(){
        lessonsController.getLesson("user1", String.valueOf(lesson2Id));
        lessonsController.getLesson("user1", String.valueOf(lesson3Id));
        lessonsController.getLesson("user2", String.valueOf(lesson3Id));
        lessonsController.getLesson("user2", String.valueOf(lesson2Id));

        progressController.updateLessonState("user1", String.valueOf(lesson2Id), lessonResult);
        progressController.updateLessonState("user2", String.valueOf(lesson3Id), lessonResult);

        progressController.cancelRental("user2", String.valueOf(lesson2Id));

        ProgressDto progressUser1 = progressController.getProgress("user1");
        ProgressDto progressUser2 = progressController.getProgress("user2");

        assertEquals(lesson3.getSummary(), progressUser1.rented.get(0));
        assertEquals(lesson2.getSummary(), progressUser1.finished.get(0));
        assertEquals(lesson3.getSummary(), progressUser2.finished.get(0));
        assertTrue(progressUser2.rented.isEmpty());
    }
}
