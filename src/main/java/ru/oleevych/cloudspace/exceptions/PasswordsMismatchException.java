package ru.oleevych.cloudspace.exceptions;

public class PasswordsMismatchException extends SignUpException{
    public PasswordsMismatchException(String message) { super (message); }
}
