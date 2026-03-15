import { useState } from "react";
import "./AddAssetModal.css";
import Input from "../../../ui/Input";
import Select from "../../../ui/Select";

import type {
  CreateAssetRequest,
  DataClassification,
  AssetType,
} from "../types/asset.types";
import Button from "../../../ui/Button";
import FormField from "../../../ui/FormField";

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
  <form className="asset-form" onSubmit={handleSubmit}>
    <h2 className="form-title">Add Asset</h2>

    <div className="form-fields">
      <FormField label="Asset name">
        <Input value={name} onChange={(e) => setName(e.target.value)} />
      </FormField>

      <FormField label="Owner">
        <Input value={owner} onChange={(e) => setOwner(e.target.value)} />
      </FormField>

      <FormField label="Asset Type">
        <Select
          value={assetType}
          onChange={(e) => setAssetType(e.target.value as AssetType)}
        >
          <option value="APPLICATION">APPLICATION</option>
          <option value="DATABASE">DATABASE</option>
          <option value="SERVER">SERVER</option>
          <option value="NETWORK_DEVICE">NETWORK_DEVICE</option>
          <option value="DOCUMENT">DOCUMENT</option>
          <option value="OTHER">OTHER</option>
        </Select>
      </FormField>

      <FormField label="Classification">
        <Select
          value={classification}
          onChange={(e) =>
            setClassification(e.target.value as DataClassification)
          }
        >
          <option value="PUBLIC">PUBLIC</option>
          <option value="INTERNAL">INTERNAL</option>
          <option value="CONFIDENTIAL">CONFIDENTIAL</option>
          <option value="RESTRICTED">RESTRICTED</option>
        </Select>
      </FormField>

      <FormField label="Description">
        <Input
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </FormField>
    </div>

    <div className="form-actions">
      <Button type="submit" disabled={saving}>
        {saving ? "Saving..." : "Add Asset"}
      </Button>
    </div>
  </form>
);
}
