package com.benzair.governancecore.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.benzair.governancecore.utils.exceptions.InvalidInputException;
import com.benzair.governancecore.utils.exceptions.ResourceNotFoundException;

/*
GlobalControllerExceptionHandler represents:
"If any of those exceptions are thrown, I decide what response to return."

It does NOT throw errors.

It catches them.
*/
// @RestControllerAdvice = formats the error response.
@RestControllerAdvice // This annotation tells Spring that this class will handle exceptions globally for all controllers.
public class GlobalControllerExceptionHandler {
    
    @ExceptionHandler(InvalidInputException.class) // This annotation tells Spring that this method should be called when an InvalidInputException is thrown.
    public ResponseEntity<ApiError> handleInvalidInputException(InvalidInputException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(ResourceNotFoundException exception){
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                         .body(new ApiError(exception.getMessage()));
    }
        
}

/*
GlobalControllerExceptionHandler

This class:

-Catches the exception
-Reads its type
-Reads its message
-Decides the HTTP status
-Builds the response body
-Sends it back to the client

It packages everything properly, so to speak.

Custom exception classes:
-Describe what went wrong.

Global exception handler:
-Decides how that error is presented to the outside world.
*/