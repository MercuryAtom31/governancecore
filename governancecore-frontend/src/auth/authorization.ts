// "import type" helps to avoid circular dependencies and ensures that we are only importing types, not actual code.
// In other words, it tells TypeScript that we only care about the shape of the data (the type) and not any runtime behavior or values from the module. 
// This can help with performance and maintainability, especially in larger codebases where circular dependencies can be an issue.
import type { AuthUser } from "./auth.types";

// With "as const", we are telling TypeScript:
// "Treat these values as fixed, exact, and read-only."
// Why we use it?
// -Prevent typos in roles
// -Enforce strict RBAC logic
// -Make arrays immutable
// -Improve type safety
const MANAGER_ROLES = ["ADMIN", "ANALYST"] as const;

export function hasRole(user: AuthUser | null, role: string): boolean {
  return user?.roles.includes(role) ?? false;
}

export function hasAnyRole(user: AuthUser | null, roles: readonly string[]): boolean {
  if (!user) {
    return false;
  }

  return roles.some((role) => user.roles.includes(role));
}

export function canManageAssets(user: AuthUser | null): boolean {
  return hasAnyRole(user, MANAGER_ROLES);
}

export function isReadOnlyUser(user: AuthUser | null): boolean {
  return !canManageAssets(user);
}
