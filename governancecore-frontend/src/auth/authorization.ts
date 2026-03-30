import type { AuthUser } from "./auth.types";

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
