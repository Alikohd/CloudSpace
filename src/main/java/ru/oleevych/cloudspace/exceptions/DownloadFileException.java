package ru.oleevych.cloudspace.exceptions;

public class DownloadFileException extends RuntimeException{
    public DownloadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
