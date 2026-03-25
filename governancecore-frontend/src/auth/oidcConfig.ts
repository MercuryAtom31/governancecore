// WebStorageStateStore =
// "Where should I store the logged-in user and tokens in the browser?"
import { WebStorageStateStore } from "oidc-client-ts";
import type { AuthProviderProps } from "react-oidc-context";

// This file defines the configuration for OpenID Connect (OIDC) authentication using the react-oidc-context library.
// The oidcConfig object contains all the necessary settings for connecting to the OIDC provider (Keycloak in this case).
export const oidcConfig: AuthProviderProps = {
  authority: import.meta.env.VITE_OIDC_AUTHORITY, // The URL of the OIDC provider (Keycloak server).
  client_id: import.meta.env.VITE_OIDC_CLIENT_ID,
  redirect_uri: import.meta.env.VITE_OIDC_REDIRECT_URI,
  post_logout_redirect_uri: import.meta.env.VITE_OIDC_POST_LOGOUT_REDIRECT_URI,
  response_type: "code", // The type of response expected from the OIDC provider.
  scope: "openid profile email", // The scope of access requested from the OIDC provider.
  // We explicitly store the authenticated OIDC user in sessionStorage.
  // This keeps the auth session limited to the current browser tab/session.
  userStore: new WebStorageStateStore({ store: window.sessionStorage }),
  // The onSigninCallback function is called after a successful sign-in.
  // It replaces the current history state to remove any query parameters or fragments added by the OIDC provider during the authentication process.
  // In other words, it cleans up the URL after the user has been redirected back from the OIDC provider, ensuring a cleaner user experience.
  onSigninCallback: () => {
    // This line replaces the current history state with a new state that has the same title and pathname, but without any query parameters or fragments.
    // Which means that after the user is redirected back from the OIDC provider, the URL will be cleaned up to remove any authentication-related parameters, providing a cleaner and more user-friendly URL.
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

// react-oidc-context stores the authenticated user under this key format.
// We reuse the same key so Axios can read the current access token for API calls.
export const oidcStorageKey = `oidc.user:${oidcConfig.authority}:${oidcConfig.client_id}`;
