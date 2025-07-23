package com.project.eventros.controllers;


import com.project.eventros.domain.dtos.ErrorDto;
import com.project.eventros.exceptions.EventNotFoundException;
import com.project.eventros.exceptions.EventUpdateException;
import com.project.eventros.exceptions.TicketTypeNotFoundException;
import com.project.eventros.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException exception) {
        log.error("constraint violation exception", exception);
        ErrorDto errorDto=new ErrorDto();
        String errorMessage=exception
                .getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation->
                        violation.getMessage()+":"+violation.getPropertyPath())
                .orElse("ConstraintViolationException Caught");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Exception Caught", ex);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An Unexpected error occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException Caught", exception);
        ErrorDto errorDto=new ErrorDto();
        BindingResult bindingResult=exception.getBindingResult();
        List<FieldError> fieldErrors=bindingResult.getFieldErrors();
        String errorMessage=fieldErrors.stream()
                .findFirst()
                .map(fieldError -> fieldError.getField()+":"+fieldError.getDefaultMessage())
                .orElse("FieldErrors Caught");
        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException exception) {
        log.error("UserNotFoundException Caught", exception);
        ErrorDto errorDto=new ErrorDto();
        errorDto.setError("User Not Found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException exception) {
        log.error("EventNotFoundException Caught", exception);
        ErrorDto errorDto=new ErrorDto();
        errorDto.setError("Event Not Found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException exception) {
        log.error("TicketTypeNotFoundException Caught", exception);
        ErrorDto errorDto=new ErrorDto();
        errorDto.setError("Ticket Type Not Found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException exception) {
        log.error("EventUpdate Exception Caught", exception);
        ErrorDto errorDto=new ErrorDto();
        errorDto.setError("exception due to event update");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
