import { Routes, Route, Link, Navigate } from "react-router-dom";
import AssetListPage from "./features/assets/pages/AssetListPage";

function App() {
  return (
    <div style={{ padding: 20 }}>
      <nav style={{ marginBottom: 20 }}>
        <Link to="/assets">Assets</Link>
      </nav>

      <Routes>
        <Route path="/" element={<Navigate to="/assets" replace />} />
        <Route path="/assets" element={<AssetListPage />} />
      </Routes>
    </div>
  );
}

export default App;