// Defines the TypeScript contract for backend error JSON.
export interface ApiError {
  timestamp: string; // when error happened
  path: string;      // request path
  httpStatus: number;    // HTTP status code example: "BAD_REQUEST"
  message: string;   // human-readable detail
}

/*
The keys (timestamp, path, etc.) must match the backend error JSON keys of the HttpErrorInfo.java.

Backend generates structured error JSON.
Frontend files exist to safely consume and display that JSON.

1) api-error.types.ts
Defines the expected error contract from backend.
Gives type safety when reading message, httpStatus, etc.

2) httpError.ts
Converts unknown caught errors into a typed ApiError | null.
Centralizes Axios error parsing so pages/components don't duplicate that logic.
*/