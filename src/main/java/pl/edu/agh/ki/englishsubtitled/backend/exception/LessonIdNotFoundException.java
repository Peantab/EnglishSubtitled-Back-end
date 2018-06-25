package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class LessonIdNotFoundException extends RuntimeException{
    public LessonIdNotFoundException(int lessonId){
        super("There is no lesson with lessonId "+lessonId+".");
    }
}
