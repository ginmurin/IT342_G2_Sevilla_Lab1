import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function TopNav() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="border-b border-slate-200 bg-white">
      <div className="mx-auto flex w-full max-w-5xl items-center justify-between px-4 py-4">
        <h2 className="text-xl font-bold text-slate-900">Protected Page</h2>
        <div className="flex items-center gap-4 text-sm font-medium">
          <Link to="/dashboard" className="text-blue-600 hover:text-blue-700">Dashboard</Link>
          <Link to="/profile" className="text-blue-600 hover:text-blue-700">View Profile</Link>
          <button
            onClick={handleLogout}
            className="rounded-lg bg-slate-900 px-3 py-1.5 text-white hover:bg-slate-700"
          >
            Logout
          </button>
        </div>
      </div>
    </header>
  );
}

export function ProtectedPage() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-slate-100">
      <TopNav />
      <main className="mx-auto w-full max-w-5xl px-4 py-8">
        <h1 className="text-3xl font-bold text-slate-900">Secure Content</h1>
        <p className="mt-3 text-slate-700">
          Hello, <span className="font-semibold">{user?.name}</span>!
        </p>
        <p className="mt-2 max-w-3xl text-slate-600">
          This page is only available to authenticated users. If you are not logged in,
          access is redirected to the login page.
        </p>

        <div className="mt-6 rounded-xl border border-blue-200 bg-blue-50 p-5">
          <h3 className="text-lg font-semibold text-blue-900">Protected Content</h3>
          <p className="mt-2 text-blue-800">
            This area contains sensitive information that requires authentication.
          </p>
        </div>
      </main>
    </div>
  );
}
