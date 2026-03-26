import "./AssetListPage.css";
import Card from "../../../ui/Card";
import { useCurrentUser } from "../../../auth/useCurrentUser";
import AddAssetModal from "../components/AddAssetModal";
import { useAssets } from "../hooks/useAssets";

export default function AssetListPage() {
  const { assets, loading, error, addAsset } = useAssets();
  const { currentUser } = useCurrentUser();

  const canManageAssets =
    currentUser?.roles.includes("ADMIN") || currentUser?.roles.includes("ANALYST");

  return (
    <div className="assets-page">
      <div className="assets-page__center-div">
        <h1 className="assets-page__title">Assets</h1>
        <p className="assets-page__lead">
          Track the systems, applications, and data stores that matter to your
          governance lifecycle.
        </p>
      </div>

      {canManageAssets ? (
        <Card className="assets-page__form-card">
          <AddAssetModal onCreate={addAsset} />
        </Card>
      ) : (
        <Card className="assets-page__form-card assets-page__read-only-card">
          <p className="assets-page__read-only-message">
            Your current role is read-only. You can review assets, but only analysts and admins can create them.
          </p>
        </Card>
      )}

      {loading && <p>Loading...</p>}
      {error && <p>{error}</p>}

      {!loading && assets.length === 0 && (
        <p className="assets-page__empty">
          Assets form the foundation of your governance lifecycle.
        </p>
      )}

      {!loading && assets.length > 0 && (
        <ul className="assets-page__grid">
          {assets.map((asset) => (
            <li key={asset.assetId}>
              <Card className="asset-card">
                <div className="asset-card__header">
                  <h2 className="asset-card__title">{asset.name}</h2>
                  <p className="asset-card__owner">{asset.owner}</p>
                </div>

                <div className="asset-card__badges">
                  <span className="asset-card__badge asset-card__badge--primary">
                    {asset.assetType}
                  </span>
                  <span className="asset-card__badge asset-card__badge--muted">
                    {asset.classification}
                  </span>
                </div>

                {asset.description && (
                  <p className="asset-card__description">{asset.description}</p>
                )}
              </Card>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
