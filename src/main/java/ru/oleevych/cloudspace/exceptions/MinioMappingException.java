package ru.oleevych.cloudspace.exceptions;

public class MinioMappingException extends RuntimeException{
    public MinioMappingException(String message, Exception cause) {
        super(message, cause);
    }
}
