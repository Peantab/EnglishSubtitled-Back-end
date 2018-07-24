package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdInvalidException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdNotFoundException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;
import pl.edu.agh.ki.englishsubtitled.backend.service.TranslationService;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private LessonRepository lessonRepository;
    private TranslationService translationService;

    @Autowired
    LessonsController(LessonRepository lessonRepository, TranslationService translationService){
        this.lessonRepository = lessonRepository;
        this.translationService = translationService;
    }

    private List<Translation> getOrCreateTranslations(List<TranslationDto> dtos){
        return dtos.stream().map(dto -> translationService.getOrCreateTranslation(dto.engWord, dto.plWord)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LessonSummaryDto> getLessons(){

        List<LessonSummaryDto> lessonSummaries = new LinkedList<>();
        for (Lesson lesson: lessonRepository.findAll()){
            lessonSummaries.add(lesson.getSummary());
        }
        return lessonSummaries;
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

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addLessons(@RequestBody List<LessonDto> lessons){
        for (LessonDto lessonDto: lessons){
            List<Translation> translations = getOrCreateTranslations(lessonDto.getTranslations());
            Lesson lesson = new Lesson(lessonDto.lessonTitle, lessonDto.filmTitle, translations);
            lessonRepository.save(lesson);
        }
    }
}
