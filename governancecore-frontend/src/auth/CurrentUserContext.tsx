import { createContext, useEffect, useMemo, useState } from "react";
import type { PropsWithChildren } from "react";
import { useAuth } from "react-oidc-context";

import { getCurrentUser } from "./authApi";
import type { AuthUser } from "./auth.types";

type CurrentUserContextValue = {
  currentUser: AuthUser | null;
  loading: boolean;
  error: string | null;
  refreshCurrentUser: () => Promise<void>;
};

// We initialize the context with undefined and enforce that it must be used within a provider.
// This means that if a component tries to use the context without being wrapped in the provider, it will throw an error, which helps catch mistakes early.
export const CurrentUserContext = createContext<CurrentUserContextValue | undefined>(undefined);

export function CurrentUserProvider({ children }: PropsWithChildren) {
  // We use the useAuth hook from react-oidc-context to get the authentication state of the user.
  const auth = useAuth();
  const [currentUser, setCurrentUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refreshCurrentUser = async () => {
    setLoading(true);
    setError(null);

    try {
      // This function calls the backend API to get the current user's information.
      const user = await getCurrentUser();
      // If the request is successful, we update the currentUser state with the retrieved user information.
      setCurrentUser(user);
    } catch (err) {
      const message = err instanceof Error ? err.message : "Failed to load current user";
      setError(message);
      setCurrentUser(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!auth.isAuthenticated) {
      setCurrentUser(null);
      setError(null);
      setLoading(false);
      return;
    }

    void refreshCurrentUser();
  }, [auth.isAuthenticated]);

  const value = useMemo(
    () => ({ currentUser, loading, error, refreshCurrentUser }),
    [currentUser, loading, error],
  );

  return <CurrentUserContext.Provider value={value}>{children}</CurrentUserContext.Provider>;
}

/*

This file defines a React context for the current authenticated user's information.

A "context" in React is a way to share data across the entire component tree without having to pass props down manually at every level.

Why do we need this?

- We want to know who the current user is (their username, email, roles) in many places in our app.
- We don't want to call the backend API to get the user info in every component that needs it.
- Instead, we create a context that fetches the user info once and provides it to any component that needs it.

How does it work?

1. CurrentUserProvider: This component fetches the current user's information from the backend when the user is authenticated. 
It manages loading and error states as well.
It provides the current user data and a function to refresh it to its children via React context.

In summary, this provider centralizes the logic for fetching and providing the current authenticated user's information across the entire frontend app.
*/
