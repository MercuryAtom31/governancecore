package com.benzair.governancecore.exceptions;

public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
        super(message);
    }

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