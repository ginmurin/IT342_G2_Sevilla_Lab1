import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Shield, User, LayoutDashboard } from 'lucide-react';

export function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center space-x-2">
              <LayoutDashboard className="w-5 h-5 text-blue-600" />
              <h1 className="text-xl">Dashboard</h1>
            </div>
            <div className="flex items-center space-x-4">
              <Link
                to="/profile"
                className="flex items-center space-x-2 text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md hover:bg-gray-100"
              >
                <User className="w-5 h-5" />
                <span>View Profile</span>
              </Link>
              <Link
                to="/protected"
                className="flex items-center space-x-2 text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md hover:bg-gray-100"
              >
                <Shield className="w-5 h-5" />
                <span>Protected Page</span>
              </Link>
              <button
                onClick={handleLogout}
                className="flex items-center space-x-2 px-4 py-2 rounded-md text-white bg-red-600 hover:bg-red-700"
              >
                <LogOut className="w-4 h-4" />
                <span>Logout</span>
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h2 className="text-2xl mb-2">Welcome back, {user?.firstName} {user?.lastName}!</h2>
            <p className="text-gray-600">
              @{user?.username} • This is your dashboard. Navigate using the menu above.
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-6">
            <Link
              to="/profile"
              className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow group"
            >
              <User className="w-12 h-12 text-blue-600 mb-4 group-hover:scale-110 transition-transform" />
              <h3 className="text-lg mb-2">View Profile</h3>
              <p className="text-sm text-gray-600">
                View and manage your profile information
              </p>
            </Link>

            <Link
              to="/protected"
              className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow group"
            >
              <Shield className="w-12 h-12 text-green-600 mb-4 group-hover:scale-110 transition-transform" />
              <h3 className="text-lg mb-2">Protected Pages</h3>
              <p className="text-sm text-gray-600">
                Access secure content available only to authenticated users
              </p>
            </Link>

            <div className="bg-white rounded-lg shadow p-6">
              <LayoutDashboard className="w-12 h-12 text-purple-600 mb-4" />
              <h3 className="text-lg mb-2">Dashboard</h3>
              <p className="text-sm text-gray-600">
                Your central hub for all activities
              </p>
            </div>
          </div>

          <div className="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-6">
            <h3 className="font-medium text-blue-900 mb-2">Quick Stats</h3>
            <div className="grid grid-cols-3 gap-4 text-center">
              <div>
                <p className="text-2xl text-blue-600">
                  {user?.isActive ? 'Active' : 'Inactive'}
                </p>
                <p className="text-sm text-blue-800">Account Status</p>
              </div>
              <div>
                <p className="text-2xl text-blue-600 capitalize">{user?.role || 'USER'}</p>
                <p className="text-sm text-blue-800">User Role</p>
              </div>
              <div>
                <p className="text-2xl text-blue-600">100%</p>
                <p className="text-sm text-blue-800">Access Level</p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

