import { api } from "../../../lib/axios";
import { toApiError } from "../../../shared/utils/httpError";
import type { Asset, CreateAssetRequest } from "../types/asset.types";

// Matches the AssetController's @RequestMapping("/api/v1/assets")
const ASSETS_PATH = "/api/v1/assets";
/*
"async" means:
"This function performs an operation that takes time and will return its result later."

Because the operation is:
Frontend → Axios → Backend → Database → Back to frontend

We wrap the result in a Promise (Promise<Asset[]>).
Which means:
"I promise that later, when the server responds, you will receive an array of Asset objects."
*/
export async function getAllAssets(): Promise<Asset[]> {
  try {
    // The following means: "Make a GET request, and assume the response body matches Asset[]."
    // await means:
    // "Pause this function until the Promise finishes, then give me the result."
    const res = await api.get<Asset[]>(ASSETS_PATH);
    return res.data;
  } catch (err) {
    const apiErr = toApiError(err);
    // Useful for RBAC later: 401/403 can be handled here or in an interceptor
    // The nullish coalescing operator (??) means:
    // "If the value on the left is null or undefined, use the value on the right instead."
    throw new Error(apiErr?.message ?? "Failed to load assets");
  }
}

export async function createAsset(payload: CreateAssetRequest): Promise<Asset> {
  try {
    const res = await api.post<Asset>(ASSETS_PATH, payload);
    return res.data;
  } catch (err) {
    const apiErr = toApiError(err);
    throw new Error(apiErr?.message ?? "Failed to create asset");
  }
}