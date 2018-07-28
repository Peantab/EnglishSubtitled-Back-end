package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.List;
import java.util.Objects;

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

    public LessonDto(){} //For Jackson to parse JSON

    public Integer getLessonId(){ //For Jackson to generate a field
        return lessonId;
    }

    public List<TranslationDto> getTranslations(){
        return translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonDto lessonDto = (LessonDto) o;
        return Objects.equals(lessonTitle, lessonDto.lessonTitle) &&
                Objects.equals(filmTitle, lessonDto.filmTitle) &&
                Objects.equals(translations, lessonDto.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonTitle, filmTitle, translations);
    }
}
