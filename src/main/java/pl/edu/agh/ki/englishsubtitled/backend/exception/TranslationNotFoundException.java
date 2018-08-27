package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class TranslationNotFoundException extends RuntimeException{
    public TranslationNotFoundException(String englishWord, String polishWord){
        super(String.format("There is no translation from '%s' to '%s'.", englishWord, polishWord));
    }
}
