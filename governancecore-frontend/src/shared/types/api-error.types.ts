// Defines the TypeScript contract for backend error JSON.
export interface ApiError {
  timestamp: string; // when error happened
  path: string;      // request path
  status: number;    // HTTP status code
  error: string;     // status text/category
  message: string;   // human-readable detail
}