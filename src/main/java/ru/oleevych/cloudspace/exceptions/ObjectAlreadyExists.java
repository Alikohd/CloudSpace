package ru.oleevych.cloudspace.exceptions;

public class ObjectAlreadyExists extends RuntimeException{
    public ObjectAlreadyExists(String message) {
        super(message);
    }
}
