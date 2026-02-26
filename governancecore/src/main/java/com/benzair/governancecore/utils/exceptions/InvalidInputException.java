package com.benzair.governancecore.utils.exceptions;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message){
        super(message);
    }
    
    public InvalidInputException(String message, Throwable cause){
        super(message, cause);
    }
}

/*
Custom Exception Classes

They do two things:

Identify what kind of error happened
(Not Found, Invalid Input, Conflict, Forbidden, etc.)

Carry context information (the message)

Example:
throw new ResourceNotFoundException("Asset not found with id: " + id);

That means:

Category of error → ResourceNotFoundException
Specific context → "Asset not found with id: 123"

It does NOT:

-Print
-Log automatically
-Return HTTP
-Build JSON

It just carries the error upward.
*/