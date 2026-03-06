import { useEffect, useState } from "react";
import type { Asset, CreateAssetRequest } from "../types/asset.types";
import { getAllAssets, createAsset } from "../api/assetApi";

export function useAssets() {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = async () => {
    setLoading(true);
    setError(null);

    try {
      const data = await getAllAssets();
      setAssets(Array.isArray(data) ? data : []);
    } catch (e) {
      const message = e instanceof Error ? e.message : "Failed to load assets";
      setError(message);
      setAssets([]); // keep UI safe
    } finally {
      setLoading(false);
    }
  };

  const addAsset = async (payload: CreateAssetRequest) => {
    setError(null);

    try {
      const created = await createAsset(payload);
      setAssets((prev) => [created, ...prev]);
    } catch (e) {
      const message = e instanceof Error ? e.message : "Failed to create asset";
      setError(message);
      throw e;
    }
  };

  useEffect(() => {
    refresh();
  }, []);

  return { assets, loading, error, refresh, addAsset };
}