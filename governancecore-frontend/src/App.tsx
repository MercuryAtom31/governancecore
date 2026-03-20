import { Routes, Route, Link, Navigate } from "react-router-dom";
import { useAuth } from "react-oidc-context";
import AuthGate from "./auth/AuthGate";
import AssetListPage from "./features/assets/pages/AssetListPage";

function App() {
  const auth = useAuth();

  return (
    <AuthGate>
      <div>
        <nav>
          <Link to="/assets">Assets</Link>
          <button type="button" onClick={() => void auth.signoutRedirect()}>
            Sign out
          </button>
        </nav>

        <Routes>
          <Route path="/" element={<Navigate to="/assets" replace />} />
          <Route path="/assets" element={<AssetListPage />} />
        </Routes>
      </div>
    </AuthGate>
  );
}

export default App;
