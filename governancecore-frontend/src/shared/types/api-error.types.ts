// Defines the TypeScript contract for backend error JSON.
export interface ApiError {
  timestamp: string; // when error happened
  path: string;      // request path
  httpStatus: number;    // HTTP status code example: "BAD_REQUEST"
  message: string;   // human-readable detail
}