package fr.uge.confroid.web.service.confroidstorageservice.exceptions;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
        super("Authentication failed");
    }
}
