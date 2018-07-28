package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class FilmTitleNotFoundException extends RuntimeException{
    public FilmTitleNotFoundException(String filmTitle){
        super("There is no film with a title \"" + filmTitle + "\".");
    }
}
