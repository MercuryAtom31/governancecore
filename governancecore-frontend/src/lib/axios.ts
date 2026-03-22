// The following class is a shared Axios client.
// Axios is a JavaScript library that lets our frontend talk to our backend.
import axios from "axios";

import { getAccessToken } from "../auth/tokenStorage";

// axios.create() = builds a configured client
export const api = axios.create({
// What Does ?? "" Mean?
// This is the nullish coalescing operator.
// It means:
// "If the value is null or undefined, use the value on the right instead."
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "",
  // keep false unless we're using cookies/session auth
  withCredentials: false,
});

// Before each request, Axios checks whether the frontend currently has an OIDC access token.
// If it does, Axios adds the Authorization header automatically.
api.interceptors.request.use((config) => {
  const token = getAccessToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

/*
A shared Axios client is a single, pre-configured HTTP client that our entire frontend uses to talk to your backend.

Instead of doing this everywhere:
axios.get("http://localhost:8080/api/v1/assets")

We create one central configuration.
*/
