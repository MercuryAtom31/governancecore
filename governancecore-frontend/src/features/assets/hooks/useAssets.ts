import { useEffect, useState } from "react";
import type { Asset, CreateAssetRequest } from "../types/asset.types";
import { getAllAssets, createAsset } from "../api/assetApi";

type UseAssetsResult = {
  assets: Asset[];
  loading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
  addAsset: (payload: CreateAssetRequest) => Promise<void>;
};

export function useAssets(): UseAssetsResult {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = async () => {
    setLoading(true);
    setError(null);

    try {
      const data = await getAllAssets();
      setAssets(data);
    } catch (e) {
      // Your API layer should throw Error("message") on failure
      const message = e instanceof Error ? e.message : "Failed to load assets";
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  const addAsset = async (payload: CreateAssetRequest) => {
    setError(null);

    try {
      const created = await createAsset(payload);

      // Update UI immediately (no full refetch needed for MVP)
      setAssets((prev) => [created, ...prev]);
    } catch (e) {
      const message = e instanceof Error ? e.message : "Failed to create asset";
      setError(message);
      throw e; // lets the UI decide (close modal? show toast? keep open?)
    }
  };

  // Fetch once when the page mounts
  useEffect(() => {
    refresh();
  }, []);

  return { assets, loading, error, refresh, addAsset };
}