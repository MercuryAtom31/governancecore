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

export type AssetType =
  | "APPLICATION"
  | "DATABASE"
  | "SERVER"
  | "NETWORK_DEVICE"
  | "DOCUMENT"
  | "OTHER";

  // Domain interface (Asset interface) → based on ResponseModel (AssetResponseModel)
  // These must match the JSON returned.
export interface Asset { 
    assetId: string; 
    name: string; 
    assetType: AssetType; 
    owner: string; 
    classification: DataClassification; 
    description?: string;
    createdAt: string;      // ISO datetime string
    updatedAt: string;      // ISO datetime string 
} 

// Request interface (CreateAssetRequest) → based on AssetRequestModel
export interface CreateAssetRequest { 
    name: string; 
    assetType: AssetType; 
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
-What the frontend sends when creating an 

A contract is the agreement between your frontend and backend about exactly what JSON shape is sent and received over HTTP.
*/