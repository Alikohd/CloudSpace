package ru.oleevych.cloudspace.exceptions;

public class UserAlreadyExistsException extends SignUpException{
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
