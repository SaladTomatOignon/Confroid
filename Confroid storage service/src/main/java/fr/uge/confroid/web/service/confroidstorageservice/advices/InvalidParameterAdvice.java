package fr.uge.confroid.web.service.confroidstorageservice.advices;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidParameterAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String ConfigurationNotFoundAdvice(InvalidParameterException ex) {
        return ex.getMessage();
    }
}
