package pl.edu.agh.ki.englishsubtitled.backend.dto;

public class LessonSummaryDto {
    Integer lessonId;
    public String lessonTitle;
    public String filmTitle;

    public LessonSummaryDto(int lessonId, String lessonTitle, String filmTitle){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.filmTitle = filmTitle;
    }

    public Integer getLessonId(){
        return lessonId;
    }
}
