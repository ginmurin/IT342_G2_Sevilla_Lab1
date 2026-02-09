import { Link, useNavigate } from 'react-router-dom';
import { LayoutGrid, User, Shield, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-slate-100">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex w-full max-w-6xl items-center justify-between px-4 py-4">
          <div className="flex items-center gap-2 text-slate-900">
            <LayoutGrid className="h-5 w-5 text-blue-600" />
            <span className="text-2xl font-medium">Dashboard</span>
          </div>

          <div className="flex items-center gap-8 text-slate-700">
            <Link to="/profile" className="inline-flex items-center gap-2 text-base hover:text-slate-900">
              <User className="h-4 w-4" />
              View Profile
            </Link>
            <Link to="/protected" className="inline-flex items-center gap-2 text-base hover:text-slate-900">
              <Shield className="h-4 w-4" />
              Protected Page
            </Link>
            <button
              onClick={handleLogout}
              className="inline-flex items-center gap-2 rounded-xl bg-red-600 px-4 py-2 text-base font-medium text-white hover:bg-red-700"
            >
              <LogOut className="h-4 w-4" />
              Logout
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto w-full max-w-6xl px-4 py-8">
        <section className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
          <h1 className="text-4xl font-semibold text-slate-900">Welcome back, {user?.name}!</h1>
          <p className="mt-2 text-slate-600">This is your dashboard. Navigate using the menu above.</p>
        </section>

        <section className="mt-6 grid gap-6 md:grid-cols-3">
          <Link to="/profile" className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200 hover:ring-blue-300">
            <User className="h-12 w-12 text-blue-600" />
            <h3 className="mt-5 text-2xl font-medium text-slate-900">View Profile</h3>
            <p className="mt-2 text-slate-600">View and manage your profile information</p>
          </Link>

          <Link to="/protected" className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200 hover:ring-emerald-300">
            <Shield className="h-12 w-12 text-emerald-600" />
            <h3 className="mt-5 text-2xl font-medium text-slate-900">Protected Pages</h3>
            <p className="mt-2 text-slate-600">Access secure content available only to authenticated users</p>
          </Link>

          <div className="rounded-2xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
            <LayoutGrid className="h-12 w-12 text-violet-600" />
            <h3 className="mt-5 text-2xl font-medium text-slate-900">Dashboard</h3>
            <p className="mt-2 text-slate-600">Your central hub for all activities</p>
          </div>
        </section>

        <section className="mt-6 rounded-2xl border border-blue-200 bg-blue-50 p-6">
          <h3 className="text-3xl font-semibold text-blue-900">Quick Stats</h3>
          <div className="mt-4 grid grid-cols-3 gap-4 text-center">
            <div>
              <p className="text-3xl font-medium text-blue-700">1</p>
              <p className="text-base text-blue-800">Active Session</p>
            </div>
            <div>
              <p className="text-3xl font-medium text-blue-700">3</p>
              <p className="text-base text-blue-800">Available Pages</p>
            </div>
            <div>
              <p className="text-3xl font-medium text-blue-700">100%</p>
              <p className="text-base text-blue-800">Access Level</p>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}

