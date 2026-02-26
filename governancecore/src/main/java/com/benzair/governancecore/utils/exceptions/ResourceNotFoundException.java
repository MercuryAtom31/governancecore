package com.benzair.governancecore.utils.exceptions;

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
public class ResourceNotFoundException extends RuntimeException {

    /*
    What does this do?
    It allows us to create the exception with a custom message.
    throw new ResourceNotFoundException("Asset with ID 123 not found");
    */
        public ResourceNotFoundException(String message) {
        super(message);
    }

    /*
    This version allows us to include:
    -A message
    -The original exception that caused this one
    */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
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