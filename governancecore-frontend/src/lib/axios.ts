// The following class is a shared Axios client.
// Axios is a JavaScript library that lets our frontend talk to our backend.
import axios from "axios";

// axios.create() = builds a configured client
export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "",
  // keep false unless we're using cookies/session auth
  withCredentials: false,
});

/*
A shared Axios client is a single, pre-configured HTTP client that our entire frontend uses to talk to your backend.

Instead of doing this everywhere:
axios.get("http://localhost:8080/api/v1/assets")

We create one central configuration.
*/
