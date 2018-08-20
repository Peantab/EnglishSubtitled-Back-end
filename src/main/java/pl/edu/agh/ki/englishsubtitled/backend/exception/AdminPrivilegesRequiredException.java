package pl.edu.agh.ki.englishsubtitled.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class AdminPrivilegesRequiredException extends RuntimeException {
    public AdminPrivilegesRequiredException(){
        super("Admin privileges are required for this operation.");
    }
}
