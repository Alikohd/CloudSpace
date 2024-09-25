package ru.oleevych.cloudspace.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.oleevych.cloudspace.exceptions.UserAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {
}