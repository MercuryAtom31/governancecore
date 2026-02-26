package com.benzair.governancecore.utils;

/*
GlobalControllerExceptionHandler represents:
"If any of those exceptions are thrown, I decide what response to return."

It does NOT throw errors.

It catches them.
*/
public class GlobalControllerExceptionHandler {
    
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