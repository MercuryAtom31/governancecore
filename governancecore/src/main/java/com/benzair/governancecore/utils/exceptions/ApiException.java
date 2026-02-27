package com.benzair.governancecore.utils.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException{

    private final HttpStatus status;

    public ApiExcepton(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){return status;}
}
