package ru.oleevych.cloudspace.exceptions;

public class PasswordsMismatchException extends RuntimeException{
    public PasswordsMismatchException(String message) { super (message); }
}
