package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class LessonPostingException extends RuntimeException{
    public LessonPostingException(Throwable cause){
        super("Invalid lesson adding operation. You're probably trying to insert an already existing lesson – for updating use HTTP PUT instead. You might also have sent a single object instead of a list (possibly of one object).", cause);
    }
}
