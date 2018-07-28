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
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;
import pl.edu.agh.ki.englishsubtitled.backend.service.FilmService;
import pl.edu.agh.ki.englishsubtitled.backend.service.TranslationService;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private final LessonRepository lessonRepository;
    private final TranslationService translationService;
    private final FilmService filmService;

    @Autowired
    LessonsController(LessonRepository lessonRepository, TranslationService translationService, FilmService filmService){
        this.lessonRepository = lessonRepository;
        this.translationService = translationService;
        this.filmService = filmService;
    }

    private List<Translation> getOrCreateTranslations(List<TranslationDto> dtos){
        return dtos.stream().map(dto -> translationService.getOrCreateTranslation(dto.engWord, dto.plWord)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LessonSummaryDto> getLessons(){
        return lessonRepository.findAll().stream().map(Lesson::getSummary).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.GET)
    public LessonDto getLesson(@PathVariable String lessonId){
        int lessonIdInt;
        try {
            lessonIdInt = Integer.parseInt(lessonId);
        } catch(NumberFormatException e){
            throw new LessonIdInvalidException(lessonId);
        }

        Optional<Lesson> lesson = lessonRepository.findById(lessonIdInt);

        if(!lesson.isPresent()) {
            throw new LessonIdNotFoundException(lessonIdInt);
        }
        return lesson.get().getDto();
    }

    @RequestMapping(path = "/{lessonId}", method = RequestMethod.DELETE)
    public void removeLesson(@PathVariable String lessonId){
        int lessonIdInt;
        try {
            lessonIdInt = Integer.parseInt(lessonId);
        } catch(NumberFormatException e){
            throw new LessonIdInvalidException(lessonId);
        }

        try {
            lessonRepository.deleteById(lessonIdInt);
        }catch (EmptyResultDataAccessException e){
            throw new LessonIdNotFoundException(lessonIdInt);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addLessons(@RequestBody List<LessonDto> lessons){
        for (LessonDto lessonDto: lessons){
            createLesson(lessonDto);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateLessons(@RequestBody List<LessonDto> lessons){
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
