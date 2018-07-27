package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer lessonId;

    @Column(unique = true, nullable = false)
    public String lessonTitle;

    @Column(nullable = false)
    public String filmTitle;

    @ManyToMany
    public List<Translation> translations = new LinkedList<>();

    public Lesson(){}

    public Lesson(String lessonTitle, String filmTitle, List<Translation> translations){
        this.lessonTitle = lessonTitle;
        this.filmTitle = filmTitle;
        this.translations = translations;
    }

    public LessonSummaryDto getSummary(){
        return new LessonSummaryDto(lessonId, lessonTitle, filmTitle);
    }

    public LessonDto getDto(){
        List<TranslationDto> translationsDto = translations.stream().map(Translation::getDto).collect(Collectors.toList());
        return new LessonDto(lessonId, lessonTitle, filmTitle, translationsDto);
    }

    public Integer getLessonId() {
        return lessonId;
    }
}
