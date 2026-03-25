export interface AuthUser {
  username: string;
  email: string;
  roles: string[];
}
/*
This file defines the shape of the current user data in the frontend.

Just like a contract.

It tells TypeScript:
"When we talk about the authenticated app user, we expect an object with:
- a username (string)
- an email (string)
- a list of roles (array of strings)"

Why this matters?
Without this contract, different files might assume different shapes for the user object.
One file might expect name, another username, another role instead of roles.

This interface keeps everything consistent.
*/