import { Routes, Route, Link, Navigate } from "react-router-dom";
import { useAuth } from "react-oidc-context";
import AuthGate from "./auth/AuthGate";
import { CurrentUserProvider } from "./auth/CurrentUserContext";
import { useCurrentUser } from "./auth/useCurrentUser";
import AssetListPage from "./features/assets/pages/AssetListPage";
import "./App.css";

function AppLayout() {
  const auth = useAuth();
  // We use the useCurrentUser hook to get the current user's information and loading state from the CurrentUserContext.
  const { currentUser, loading } = useCurrentUser();

  return (
    <div>
      <nav>
        <Link to="/assets">Assets</Link>
        {/* If the user information is still loading, we show "Loading user...".
        Otherwise, if we have the current user's information, we display their username. */}
        {loading ? <span>Loading user...</span> : currentUser && <span>{currentUser.username}</span>}
        <button className="signOutButton" type="button" onClick={() => void auth.signoutRedirect()}>
          Sign out
        </button>
      </nav>

      <Routes>
        <Route path="/" element={<Navigate to="/assets" replace />} />
        <Route path="/assets" element={<AssetListPage />} />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <AuthGate>
      <CurrentUserProvider>
        <AppLayout />
      </CurrentUserProvider>
    </AuthGate>
  );
}

export default App;

