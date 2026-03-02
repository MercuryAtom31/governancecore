/*
What Do the Vertical Lines | Mean?

The vertical line is called a pipe.
The | symbol is called a union type in TypeScript.
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

export interface Asset { 
    id: string; 
    name: string; 
    type: string; 
    owner: string; 
    classification: DataClassification; 
    description?: string; 
} 

export interface CreateAssetRequest { 
    name: string; 
    type: string; 
    owner: string; 
    classification: DataClassification; 
    description?: string; 
}

/*
This file is the contract of the Asset feature.
They are grouped together because they all describe the same domain concept: Asset.

It defines:

-What an Asset looks like
-What data classification is allowed
-What the frontend sends when creating an asset
*/