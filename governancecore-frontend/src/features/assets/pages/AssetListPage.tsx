import { useAssets } from "../hooks/useAssets";

export default function AssetListPage() {
  const { assets, loading, error } = useAssets();

  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Assets</h1>

      {loading && <p>Loading...</p>}

      {error && <p className="text-red-500">{error}</p>}

      {!loading && assets.length === 0 && (
        <p className="text-gray-400">
          Assets form the foundation of your governance lifecycle.
        </p>
      )}

      {!loading && assets.length > 0 && (
        <ul>
          {assets.map((asset) => (
            <li key={asset.assetId}>
              {asset.name} — {asset.owner}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}