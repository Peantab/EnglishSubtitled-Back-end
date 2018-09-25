package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class LessonNotRentedException extends RuntimeException{
    public LessonNotRentedException(int lessonId){
        super("You have not rented a lesson with id " + lessonId + ".");
    }
}
