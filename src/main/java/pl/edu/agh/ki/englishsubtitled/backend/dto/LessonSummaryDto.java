package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.Objects;

public class LessonSummaryDto {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    public LessonSummaryDto(int lessonId, String lessonTitle, String filmTitle){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.filmTitle = filmTitle;
    }

    public LessonSummaryDto(){} // For Jackson

    public Integer getLessonId(){ // For Jackson
        return lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonSummaryDto that = (LessonSummaryDto) o;
        return Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(lessonTitle, that.lessonTitle) &&
                Objects.equals(filmTitle, that.filmTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, lessonTitle, filmTitle);
    }
}
