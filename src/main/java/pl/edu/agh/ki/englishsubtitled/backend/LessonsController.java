package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
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

        List<TranslationDto> lesson1TranslationDtos = Collections.emptyList();
        List<TranslationDto> lesson2TranslationDtos = new LinkedList<>();

        lesson2TranslationDtos.add(new TranslationDto("home", "dom"));
        lesson2TranslationDtos.add(new TranslationDto("bag", "torba"));
        lesson2TranslationDtos.add(new TranslationDto("computer", "komputer"));
        lesson2TranslationDtos.add(new TranslationDto("bike", "rower"));
        lesson2TranslationDtos.add(new TranslationDto("dog", "pies"));
        lesson2TranslationDtos.add(new TranslationDto("cat", "kot"));
        lesson2TranslationDtos.add(new TranslationDto("frog", "żaba"));
        lesson2TranslationDtos.add(new TranslationDto("bed", "łóżko"));
        lesson2TranslationDtos.add(new TranslationDto("leg", "noga"));
        lesson2TranslationDtos.add(new TranslationDto("sleep", "spać"));
        lesson2TranslationDtos.add(new TranslationDto("nose", "nos"));
        lesson2TranslationDtos.add(new TranslationDto("eat", "jeść"));
        lesson2TranslationDtos.add(new TranslationDto("talk", "mówić"));
        lesson2TranslationDtos.add(new TranslationDto("live", "żyć"));

        List<Translation> lesson1Translations = getOrCreateTranslations(lesson1TranslationDtos);
        List<Translation> lesson2Translations = getOrCreateTranslations(lesson2TranslationDtos);

        Lesson lesson1 = new Lesson("Big Buck Bunny - full transcription", "Big Buck Bunny", lesson1Translations);
        Lesson lesson2 = new Lesson("Mock Movie with words by Justyna", "Mock Movie", lesson2Translations);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
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
}
