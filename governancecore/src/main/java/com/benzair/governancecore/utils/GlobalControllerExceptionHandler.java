package com.benzair.governancecore.utils;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.benzair.governancecore.utils.exceptions.ApiException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpErrorInfo> handleApiException(ApiException exception, WebRequest request) {
        HttpStatus status = exception.getStatus();
        return ResponseEntity.status(status)
                .body(new HttpErrorInfo(status, request.getDescription(false), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpErrorInfo> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HttpErrorInfo(HttpStatus.BAD_REQUEST, request.getDescription(false), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorInfo> handleUnexpectedException(Exception exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, request.getDescription(false), "Unexpected server error"));
    }
}