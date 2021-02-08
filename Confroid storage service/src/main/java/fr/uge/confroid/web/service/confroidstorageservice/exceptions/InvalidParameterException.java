package fr.uge.confroid.web.service.confroidstorageservice.exceptions;

public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String parameterName) {
        super("Required parameter missing : " + parameterName);
    }
}
