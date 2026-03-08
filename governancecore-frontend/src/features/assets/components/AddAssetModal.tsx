import { useState } from "react";
import "./AddAssetModal.css";
import type {
  CreateAssetRequest,
  DataClassification,
  AssetType,
} from "../types/asset.types";

type Props = {
  onCreate: (payload: CreateAssetRequest) => Promise<void>;
};

export default function AddAssetModal({ onCreate }: Props) {
  const [name, setName] = useState("");
  const [owner, setOwner] = useState("");
  const [assetType, setAssetType] = useState<AssetType>("APPLICATION");
  const [classification, setClassification] =
    useState<DataClassification>("INTERNAL");
  const [description, setDescription] = useState("");
  const [saving, setSaving] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    setSaving(true);

    try {
      const payload: CreateAssetRequest = {
        name,
        owner,
        assetType,
        classification,
        description: description.trim() ? description.trim() : undefined,
      };

      await onCreate(payload);

      // reset form
      setName("");
      setOwner("");
      setAssetType("APPLICATION");
      setClassification("INTERNAL");
      setDescription("");
    } finally {
      setSaving(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: 20 }}>
      <input
        placeholder="Asset name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <input
        placeholder="Owner"
        value={owner}
        onChange={(e) => setOwner(e.target.value)}
      />

      <select
        value={assetType}
        onChange={(e) => setAssetType(e.target.value as AssetType)}
      >
        <option value="APPLICATION">APPLICATION</option>
        <option value="DATABASE">DATABASE</option>
        <option value="SERVER">SERVER</option>
        <option value="NETWORK_DEVICE">NETWORK_DEVICE</option>
        <option value="DOCUMENT">DOCUMENT</option>
        <option value="OTHER">OTHER</option>
      </select>

      <select
        value={classification}
        onChange={(e) => setClassification(e.target.value as DataClassification)}
      >
        <option value="PUBLIC">PUBLIC</option>
        <option value="INTERNAL">INTERNAL</option>
        <option value="CONFIDENTIAL">CONFIDENTIAL</option>
        <option value="RESTRICTED">RESTRICTED</option>
      </select>

      <input
        placeholder="Description (optional)"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <button type="submit" disabled={saving}>
        {saving ? "Saving..." : "Add Asset"}
      </button>
    </form>
  );
}