// Converts unknown caught errors into a typed ApiError.
import axios from "axios";
//With Vite + TypeScript: we never include .ts extension.
import type { ApiError } from "../types/api-error.types";

// Accepts any thrown error.
// Returns typed ApiError if possible, else null.
export function toApiError(err: unknown): ApiError | null {
  if (axios.isAxiosError(err) && err.response?.data) {
    return err.response.data as ApiError;
  }
  return null;
}

// How they work together (httpError.ts and api-error.types.ts):

// API call fails -> catch (err) -> call toApiError(err)
// if not null, UI can display apiError.message
// if null, show generic fallback message.

/*
1) The colon means:
"This variable has this type."

2) "): ApiError | null"
This is the function return type.

It means: this function will return either:

-an ApiError
-OR null

So the colon after the parentheses means:

FunctionName(parameters): ReturnType

3) The ?. (Optional Chaining)
err.response?.data

It means:

"If response exists, then access .data.
If response is null or undefined, stop and return undefined."
*/