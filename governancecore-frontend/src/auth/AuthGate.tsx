import { useEffect } from "react";
// This ProppsWithChildren type allows the component to accept children elements, which will be rendered if the user is authenticated.
import type { PropsWithChildren } from "react";
// The following import is responsible for handling authentication with Keycloak using OpenID Connect.
import { useAuth } from "react-oidc-context";
// This function component serves as a gatekeeper for the application, ensuring that only authenticated users can access its content.
// It uses the useAuth hook to manage authentication state and redirects unauthenticated users to the Keycloak login page.
export default function AuthGate({ children }: PropsWithChildren) {
  // The useAuth hook provides access to the authentication state and methods.
  // It checks if the user is authenticated, if the authentication process is still loading, and if there are any errors.
  // If the user is not authenticated and not currently loading, it triggers a redirect to the Keycloak login page.
  const auth = useAuth();
  // This useEffect hook runs whenever the authentication state changes.
  // If the user is not authenticated and not currently loading,
  // it initiates the sign-in redirect process.
  useEffect(() => {
    if (!auth.isLoading && !auth.isAuthenticated && !auth.activeNavigator) {
      void auth.signinRedirect();
    }
  }, [auth]);
  // The component renders different content based on the authentication state:
  // If the authentication process is still loading, it displays a loading message.
  // If there is an authentication error, it displays the error message.
  // If the user is not authenticated, it shows a message indicating that a redirect to Keycloak is happening.
  // If the user is authenticated, it renders the children elements passed to the AuthGate component.
  if (auth.isLoading) {
    return <p>Loading authentication...</p>;
  }

  if (auth.error) {
    return <p>Authentication error: {auth.error.message}</p>;
  }

  if (!auth.isAuthenticated) {
    return <p>Redirecting to Keycloak...</p>;
  }
  // If the user is authenticated, it renders the children elements passed to the AuthGate component.
  // And the children elements are simply the content that should be displayed to authenticated users.
  // This allows us to wrap any part of the application with the AuthGate component to ensure that only authenticated users can access it.
  return <>{children}</>;
}
