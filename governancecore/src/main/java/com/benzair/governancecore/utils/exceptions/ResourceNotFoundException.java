package com.benzair.governancecore.utils.exceptions;

import org.springframework.http.HttpStatus;
/*
The following is a custom exception class that extends RuntimeException. 
It provides two constructors: one that accepts a message 
and another that accepts both a message and a cause (another Throwable). 
This allows us to create instances of ResourceNotFoundException 
with a specific error message and optionally include the underlying cause of the exception.
*/

/*
What is RuntimeException?

RuntimeException is a built-in Java class that represents 
unchecked exceptions — errors that happen during program execution and do NOT need to be declared or caught explicitly.
*/
public class ResourceNotFoundException extends ApiException {

    /*
    What does this do?
    It allows us to create the exception with a custom message.
    throw new ResourceNotFoundException("Asset with ID 123 not found");
    */
        public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

/*
The package statement must EXACTLY match the folder structure starting from src/main/java.

Start from src/main/java
Everything after that becomes your package name.
*/

/*
How Imports Work:

Syntax: import full.package.name.ClassName;

Example:
Your exception is in:
package com.benzair.governancecore.exceptions;

So in AssetServiceImpl you import it like this:
import com.benzair.governancecore.exceptions.ResourceNotFoundException;
*/

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