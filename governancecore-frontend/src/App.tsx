import { Routes, Route, Link, Navigate } from "react-router-dom";
import { useAuth } from "react-oidc-context";
import AuthGate from "./auth/AuthGate";
import AssetListPage from "./features/assets/pages/AssetListPage";
import "./App.css";

function App() {
  const auth = useAuth();

  return (
    // The AuthGate component is used to wrap the main content of the application,
    // ensuring that only authenticated users can access it.
    // In plain English, it means that the AuthGate component acts as a protective barrier around the main content of the app,
    // allowing only users who have successfully logged in to see and interact with the content inside it.
    <AuthGate>
      <div>
        <nav>
          <Link to="/assets">Assets</Link>
          <button className="signOutButton" type="button" onClick={() => void auth.signoutRedirect()}>
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

// We usually wrap the entire application with things that provide context or functionality that is needed throughout the app, such as authentication, theming, or state management.
// In this case, the AuthGate component is used to ensure that only authenticated users can access the main content of the application.
// By wrapping the entire app with AuthGate, we can easily manage authentication
// and protect all routes and components within the app without having to add authentication checks to each individual component or route.

// In React, wrapping the application means that we are using a higher-order component (HOC) or a context provider to provide certain functionality or data to all components within the app.
// In this case, the AuthGate component is a higher-order component that checks if the user is authenticated before allowing access to the main content of the application.
// By wrapping the entire app with AuthGate, we ensure that all routes and components within the app are protected and only accessible to authenticated users.