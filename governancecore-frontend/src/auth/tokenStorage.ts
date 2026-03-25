import { User } from "oidc-client-ts";

import { oidcStorageKey } from "./oidcConfig";

// Reads the authenticated OIDC user from sessionStorage and returns the access token.
// If no user is stored yet, we return null so Axios sends the request without a bearer token.
export function getAccessToken(): string | null {
  const storedUser = window.sessionStorage.getItem(oidcStorageKey);
  if (!storedUser) {
    return null;
  }

  const user = User.fromStorageString(storedUser);
  return user.access_token ?? null;
}
