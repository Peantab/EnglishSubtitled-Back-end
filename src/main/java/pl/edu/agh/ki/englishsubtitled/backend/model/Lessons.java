package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Paweł Taborowski on 25.06.18.
 */
public class Lessons {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;
    List<TranslationDto> translations = new LinkedList<>();

    public Lessons(int lessonId, String lessonTitle, String filmTitle){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.filmTitle = filmTitle;
    }

    public void addTranslation(String englishWord, String polishWord){
        TranslationDto translationDto = new TranslationDto(englishWord, polishWord);
        translations.add(translationDto);
    }

    public LessonSummaryDto getSummary(){
        return new LessonSummaryDto(lessonId, lessonTitle, filmTitle);
    }

    public LessonDto getDto(){
        return new LessonDto(lessonId, lessonTitle, filmTitle, translations);
    }
}
