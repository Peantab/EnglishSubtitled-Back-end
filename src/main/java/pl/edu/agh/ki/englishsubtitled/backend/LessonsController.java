package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdInvalidException;
import pl.edu.agh.ki.englishsubtitled.backend.exception.LessonIdNotFoundException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lessons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/lessons")
public class LessonsController {

    List<Lessons> lessons;

    LessonsController(){
        lessons = new ArrayList<>(2);
        Lessons lesson1 = new Lessons(1, "Big Buck Bunny - full transcription", "Big Buck Bunny");
        Lessons lesson2 = new Lessons(2, "Mock Movie with words by Justyna", "Mock Movie");

        lesson2.addTranslation("home", "dom");
        lesson2.addTranslation("bag", "torba");
        lesson2.addTranslation("computer", "komputer");
        lesson2.addTranslation("bike", "rower");
        lesson2.addTranslation("dog", "pies");
        lesson2.addTranslation("cat", "kot");
        lesson2.addTranslation("frog", "żaba");
        lesson2.addTranslation("bed", "łóżko");
        lesson2.addTranslation("leg", "noga");
        lesson2.addTranslation("sleep", "spać");
        lesson2.addTranslation("nose", "nos");
        lesson2.addTranslation("eat", "jeść");
        lesson2.addTranslation("talk", "mówić");
        lesson2.addTranslation("live", "żyć");

        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LessonSummaryDto> getLessons(){

        List<LessonSummaryDto> lessonSummaries = new LinkedList<>();
        for (Lessons lesson: lessons){
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

        LessonDto lessonDto;
        try {
            lessonDto = lessons.get(lessonIdInt - 1).getDto();
        } catch(IndexOutOfBoundsException e){
            throw new LessonIdNotFoundException(lessonIdInt);
        }
        return lessonDto;
    }
}
