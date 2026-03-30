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

// We export a function called "hasRole" that takes:
// a user (which can be an AuthUser OR null)
// a role (a string)
// and returns a boolean (true or false).

// | --> "allowed types" (design time)
// || --> "logical condition" (runtime)

// This function checks if the user has a specific role defined in the MANAGER_ROLES array.
// It uses optional chaining (?.) to safely access the roles property of the user object, and the nullish coalescing operator (??) to return false if the user is null or if the roles property is undefined.
export function hasRole(user: AuthUser | null, role: string): boolean {
// If the user exists, check if their roles include the given role.
// If the user does not exist, return false.

// user?.roles
// This is optional chaining (?.)
//   It means:
// "Only access roles if user is NOT null"
  return user?.roles.includes(role) ?? false;
}

// This function checks if the user has any of the roles specified in the "roles" array.
export function hasAnyRole(user: AuthUser | null, roles: readonly string[]): boolean {
  // If the user is null, they have no roles, so return false.
  if (!user) {
    return false;
  }
  // Check if any of the roles in the "roles" array are included in the user's roles.
  // the keyword "some" is an array method that returns true if at least one element in the array satisfies the provided testing function.
  // The keyword "includes" is an array method that checks if a certain value exists in the array and returns true or false accordingly.
  // The difference between "some" and "includes" is that "some" allows you to test a condition on each element of the array, while "includes" checks for the presence of a specific value in the array.
  return roles.some((role) => user.roles.includes(role));
}

// This function checks if the user has the necessary roles to manage assets.
export function canManageAssets(user: AuthUser | null): boolean {
  return hasAnyRole(user, MANAGER_ROLES);
}

// This function checks if the user is a read-only user, which means they do not have the necessary roles to manage assets.
export function isReadOnlyUser(user: AuthUser | null): boolean {
  return !canManageAssets(user);
}


// This whole file is about defining the authorization logic for our frontend application.
// It provides functions to check if a user has certain roles or permissions, which can then be used throughout the app to conditionally render UI elements 
// or restrict access to certain features based on the user's role.