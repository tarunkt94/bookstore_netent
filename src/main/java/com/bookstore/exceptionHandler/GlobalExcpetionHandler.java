package com.bookstore.exceptionHandler;

import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.responses.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExcpetionHandler {


    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse ValidationExceptionHandler(ValidationException vEx){
        return new GenericResponse(vEx.getMessage());
    }

    @ExceptionHandler(value = InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse InternalServerExceptionHandler(InternalServerException iSEx){
        return new GenericResponse("Internal Server Exception, try again later");
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void resourceNotFoundException(ResourceNotFoundException rNFEx){
    }
}
