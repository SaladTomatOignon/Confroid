package fr.uge.confroid.web.service.confroidstorageservice.advices;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.AuthenticationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthenticationFailedAdvice {

    @ResponseBody
    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String AuthenticationFailedAdvice(AuthenticationFailedException ex) {
        return ex.getMessage();
    }
}
