import { api } from "../lib/axios";
import { toApiError } from "../shared/utils/httpError";
import type { AuthUser } from "./auth.types";

// The path to the backend API endpoint that returns:
// "Who is the currently logged-in user?"
const AUTH_ME_PATH = "/api/v1/auth/me";

// This function calls the backend API to get the current user's information.
export async function getCurrentUser(): Promise<AuthUser> {
  try {
    // api.get() = makes a GET request to the specified endpoint.
    const res = await api.get<AuthUser>(AUTH_ME_PATH);
    // If the request is successful, the user's information will be in res.data.
    return res.data;
  } catch (err) {
    const apiErr = toApiError(err);
    throw new Error(apiErr?.message ?? "Failed to load current user");
  }
}

// This file defines the API call to get the current authenticated user's information from the backend.