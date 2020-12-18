package fr.uge.confroid.web.service.confroidstorageservice.advices;

import fr.uge.confroid.web.service.confroidstorageservice.exceptions.ConfigurationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConfigurationNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ConfigurationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ConfigurationNotFoundAdvice(ConfigurationNotFoundException ex) {
        return ex.getMessage();
    }
}
