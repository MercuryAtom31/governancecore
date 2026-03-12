import AddAssetModal from "../components/AddAssetModal";
import { useAssets } from "../hooks/useAssets";

export default function AssetListPage() {
  const { assets, loading, error, addAsset } = useAssets();

  return (
    <div>
      <h1>Assets</h1>

      <AddAssetModal onCreate={addAsset} />

      {loading && <p>Loading...</p>}
      {error && <p>{error}</p>}

      {!loading && assets.length === 0 && (
        <p>
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