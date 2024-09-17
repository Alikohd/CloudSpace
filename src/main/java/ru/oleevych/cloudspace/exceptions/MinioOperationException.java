package ru.oleevych.cloudspace.exceptions;

public class MinioOperationException extends RuntimeException {
    public MinioOperationException(Throwable cause) {
        super(cause);
    }
}
