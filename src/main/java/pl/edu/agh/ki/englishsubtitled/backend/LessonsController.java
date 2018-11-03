package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdInvalidException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdNotFoundException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonPostingException;
import pl.edu.agh.ki.englishsubtitled.backend.model.*;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonStateRepository;
import pl.edu.agh.ki.englishsubtitled.backend.service.FilmService;
import pl.edu.agh.ki.englishsubtitled.backend.service.TranslationService;
import pl.edu.agh.ki.englishsubtitled.backend.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private final LessonRepository lessonRepository;
    private final LessonStateRepository lessonStateRepository;
    private final TranslationService translationService;
    private final FilmRepository filmRepository;
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    LessonsController(LessonRepository lessonRepository, LessonStateRepository lessonStateRepository,
                      TranslationService translationService, FilmRepository filmRepository, FilmService filmService, UserService userService){
        this.lessonRepository = lessonRepository;
        this.lessonStateRepository = lessonStateRepository;
        this.translationService = translationService;
        this.filmRepository = filmRepository;
        this.filmService = filmService;
        this.userService = userService;
    }

    private List<Translation> getOrCreateTranslations(List<TranslationDto> dtos){
        return dtos.stream().map(dto -> translationService.getOrCreateTranslation(dto.engWord, dto.plWord)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LessonSummaryDto> getLessons(){
        return lessonRepository.findAll().stream().map(Lesson::getSummary).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.GET)
    public LessonDto getLesson(@RequestHeader("Authorization") String token, @PathVariable String lessonId){
        User user = userService.authenticate(token, true);
        int lessonIdInt;
        try {
            lessonIdInt = Integer.parseInt(lessonId);
        } catch(NumberFormatException e){
            throw new LessonIdInvalidException(lessonId);
        }

        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonIdInt);

        if(!lessonOptional.isPresent()) {
            throw new LessonIdNotFoundException(lessonIdInt);
        }

        if (!user.getRelatedLessons().contains(lessonOptional.get())){
            if (user.getRentedLessons().size() >= Configuration.getInstance().getRentedLessonsLimit()){
                List<LessonState> lessonStates = user.makePlaceForRentedLesson();
                lessonStateRepository.deleteAll(lessonStates);
            }

            LessonState lessonState = new LessonState(user, lessonOptional.get());
            user.addRental(lessonState);
            lessonStateRepository.saveAndFlush(lessonState);
        }
        return lessonOptional.get().getDto();
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.DELETE)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeLesson(@RequestHeader("Authorization") String token, @PathVariable String lessonId){
        userService.authenticateAdmin(token);
        int lessonIdInt;
        try {
            lessonIdInt = Integer.parseInt(lessonId);
        } catch(NumberFormatException e){
            throw new LessonIdInvalidException(lessonId);
        }

        Film parent = null;

        try {
            Lesson lesson = lessonRepository.getOne(lessonIdInt);
            Film film = lesson.getFilm();
            if (film.getLessons().size() == 1){ // This lesson only
                parent = film;
            }
        } catch (EntityNotFoundException e){
            parent = null;
        }

        try {
            lessonRepository.deleteById(lessonIdInt);
        }catch (EmptyResultDataAccessException e){
            throw new LessonIdNotFoundException(lessonIdInt);
        }

        if (parent != null){
            filmRepository.delete(parent);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addLessons(@RequestHeader("Authorization") String token, @RequestBody List<LessonDto> lessons){
        userService.authenticateAdmin(token);
        try {
            for (LessonDto lessonDto : lessons) {
                createLesson(lessonDto);
            }
            lessonRepository.flush();
        }catch (RuntimeException e){
            throw new LessonPostingException(e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateLessons(@RequestHeader("Authorization") String token, @RequestBody List<LessonDto> lessons){
        userService.authenticateAdmin(token);
        for (LessonDto lessonDto: lessons){
            Lesson existingLesson = lessonRepository.findByLessonTitleEquals(lessonDto.lessonTitle);

            if (existingLesson != null){
                if (existingLesson.getDto().equals(lessonDto)) continue;

                existingLesson.lessonTitle = lessonDto.lessonTitle;
                existingLesson.film = filmService.getOrCreateFilm(lessonDto.filmTitle);
                existingLesson.translations = getOrCreateTranslations(lessonDto.getTranslations());
                continue;
            }
            createLesson(lessonDto);
        }
    }

    private void createLesson(LessonDto lessonDto){
        List<Translation> translations = getOrCreateTranslations(lessonDto.getTranslations());
        Film film = filmService.getOrCreateFilm(lessonDto.filmTitle);
        Lesson lesson = new Lesson(lessonDto.lessonTitle, film, translations);
        lessonRepository.save(lesson);
    }
}
