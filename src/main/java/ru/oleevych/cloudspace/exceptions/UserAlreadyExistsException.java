package ru.oleevych.cloudspace.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
