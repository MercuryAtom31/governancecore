/*
What Do the Vertical Lines | Mean?

The vertical line is called a pipe.
It is the same as Java enum.

The following means:

DataClassification can be:
"PUBLIC" OR
"INTERNAL" OR
"CONFIDENTIAL" OR
"RESTRICTED"
*/
export type DataClassification =
  | "PUBLIC"
  | "INTERNAL"
  | "CONFIDENTIAL"
  | "RESTRICTED";