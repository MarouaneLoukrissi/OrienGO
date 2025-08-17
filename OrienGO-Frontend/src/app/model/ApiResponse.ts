export interface ApiResponse<T> {
  code: string;           // e.g., "SUCCESS", "USER_NOT_FOUND"
  status: number;         // HTTP status code, e.g., 200, 404
  message: string;        // Human-readable message
  data: T;                // The actual payload (can be any type)
  errors?: {              // Optional, map of error fields to messages
    [key: string]: string;
  };
}