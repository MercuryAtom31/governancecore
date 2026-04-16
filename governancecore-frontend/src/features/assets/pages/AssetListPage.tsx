import "./AssetListPage.css";
import { canManageAssets } from "../../../auth/authorization";
import { useCurrentUser } from "../../../auth/useCurrentUser";
import Card from "../../../ui/Card";
import AddAssetModal from "../components/AddAssetModal";
import { useAssets } from "../hooks/useAssets";

export default function AssetListPage() {
  const { assets, loading, error, addAsset } = useAssets();
  const { currentUser } = useCurrentUser();
  const userCanManageAssets = canManageAssets(currentUser);

  return (
    <div className="assets-page">
      <div className="assets-page__center-div">
        <h1 className="assets-page__title">Assets</h1>
        <p className="assets-page__lead">
          Track the systems, applications, and data stores that matter to your
          governance lifecycle.
        </p>
      </div>
      {userCanManageAssets ? (
        <Card className="assets-page__form-card">
          <AddAssetModal onCreate={addAsset} />
        </Card>
      ) : (
        <Card className="assets-page__form-card assets-page__read-only-card">
          <p className="assets-page__read-only-message">
            Your current role is read-only. You can review assets, but only
            analysts and admins can create them.
          </p>
        </Card>
      )}
      {/* The following line means that if the data is still loading,
      it will show "Loading...".
      If there is an error, it will display the error message.
      If there are no assets and it's not loading, it will show a message about assets being the foundation of governance.
      If there are assets, it will display them in a grid.
      The && mean "and"*/}
      {loading && <p>Loading...</p>}
      {error && <p>{error}</p>}
      {!loading && assets.length === 0 && (
        <p className="assets-page__empty">
          Assets form the foundation of your governance lifecycle.
        </p>
      )}
      {/* The following code means that if it's not loading and there are assets,
      it will display them in a grid. // Each asset will be displayed as a card
      with its name, owner, type, classification, and description (if
      available). */}
      {!loading && assets.length > 0 && (
        <ul className="assets-page__grid">
          {/* .map() is a JavaScript array method that:
          takes an array, loops through each element, returns a new array.
          Here it says:
          For each asset in the assets array, create a new list item (<li>) that contains a card with the asset's details.*/}
          {assets.map((asset) => (
            // key is a unique identifier for each item in the list, which helps React optimize rendering.
            // it is used to identify which items have changed, are added, or are removed.
            // In this case, we use asset.assetId as the key for each asset card.
            // If it detects that an asset with the same key has changed, it will only re-render that specific card instead of the entire list, improving performance.
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
