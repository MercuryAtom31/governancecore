import { useEffect } from "react";
import type { PropsWithChildren } from "react";
import { useAuth } from "react-oidc-context";

export default function AuthGate({ children }: PropsWithChildren) {
  const auth = useAuth();

  useEffect(() => {
    if (!auth.isLoading && !auth.isAuthenticated && !auth.activeNavigator) {
      void auth.signinRedirect();
    }
  }, [auth]);

  if (auth.isLoading) {
    return <p>Loading authentication...</p>;
  }

  if (auth.error) {
    return <p>Authentication error: {auth.error.message}</p>;
  }

  if (!auth.isAuthenticated) {
    return <p>Redirecting to Keycloak...</p>;
  }

  return <>{children}</>;
}
