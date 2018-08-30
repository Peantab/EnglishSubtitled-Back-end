package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.edu.agh.ki.englishsubtitled.backend.Configuration;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class LessonRentalLimitExceededException extends RuntimeException{
    public LessonRentalLimitExceededException(){
        super(String.format("You have exceeded rented lessons limit which is %d lessons. Either finish one or return it.", Configuration.getInstance().getRentedLessonsLimit()));
    }
}
