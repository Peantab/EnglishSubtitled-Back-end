package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LessonIdInvalidException extends RuntimeException {
    public LessonIdInvalidException(String s) {
        super("Invalid lessonId, expected number got \"" + s + "\".");
    }
}
