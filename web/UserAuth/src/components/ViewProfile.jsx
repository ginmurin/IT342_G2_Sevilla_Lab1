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
        <h2 className="text-xl font-bold text-slate-900">View Profile</h2>
        <div className="flex items-center gap-4 text-sm font-medium">
          <Link to="/dashboard" className="text-blue-600 hover:text-blue-700">Dashboard</Link>
          <Link to="/protected" className="text-blue-600 hover:text-blue-700">Protected</Link>
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

export function ViewProfile() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-slate-100">
      <TopNav />
      <main className="mx-auto w-full max-w-5xl px-4 py-8">
        <div className="max-w-2xl rounded-2xl bg-white p-8 shadow-sm ring-1 ring-slate-200">
          <h1 className="text-3xl font-bold text-slate-900">Profile Information</h1>

          <div className="mt-6 space-y-4 text-slate-700">
            <div>
              <p className="text-sm font-semibold text-slate-500">Full Name</p>
              <p className="mt-1 text-lg">{user?.name}</p>
            </div>
            <div>
              <p className="text-sm font-semibold text-slate-500">Email Address</p>
              <p className="mt-1 text-lg">{user?.email}</p>
            </div>
            <div>
              <p className="text-sm font-semibold text-slate-500">Account Status</p>
              <p className="mt-1 text-lg">Active</p>
            </div>
          </div>

          <div className="mt-8 border-t border-slate-200 pt-6">
            <h3 className="text-lg font-semibold text-slate-900">Access Permissions</h3>
            <ul className="mt-3 list-disc space-y-1 pl-5 text-slate-700">
              <li>Dashboard Access</li>
              <li>View Profile</li>
              <li>Access Protected Pages</li>
            </ul>
          </div>
        </div>
      </main>
    </div>
  );
}
