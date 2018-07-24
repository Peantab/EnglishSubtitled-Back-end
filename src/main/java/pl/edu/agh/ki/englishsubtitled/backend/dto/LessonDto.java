package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.List;

public class LessonDto {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    List<TranslationDto> translations;

    public LessonDto(int lessonId, String lessonTitle, String filmTitle, List<TranslationDto> translations){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.filmTitle = filmTitle;
        this.translations = translations;
    }

    public LessonDto(){}

    public Integer getLessonId(){
        return lessonId;
    }

    public List<TranslationDto> getTranslations(){
        return translations;
    }
}
