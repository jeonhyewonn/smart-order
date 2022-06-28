package com.example.smartorder.exception;

import com.example.smartorder.member.exception.AlreadyExistingMemberException;
import com.example.smartorder.member.exception.NotFoundMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleInvalidParameter(MethodArgumentNotValidException e) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST, e);

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addCauseField(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectException.class)
    public ExceptionResponse handleIncorrectResource(IncorrectException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistingMemberException.class)
    public ExceptionResponse handleAlreadyExistingMember(AlreadyExistingMemberException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotFoundMemberException.class)
    public ExceptionResponse handleNotFoundMember(NotFoundMemberException e) {
        return new ExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundResourceException.class)
    public ExceptionResponse handleNotFoundResource(NotFoundResourceException e) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomException.class)
    public ExceptionResponse handleUncaughtException(CustomException e) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
