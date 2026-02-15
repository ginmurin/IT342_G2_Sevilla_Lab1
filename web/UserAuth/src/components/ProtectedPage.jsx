import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Home, Lock, User } from 'lucide-react';

export function ProtectedPage() {
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
              <Lock className="w-5 h-5 text-blue-600" />
              <h1 className="text-xl">Protected Page</h1>
            </div>
            <div className="flex items-center space-x-4">
              <Link
                to="/dashboard"
                className="flex items-center space-x-2 text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md hover:bg-gray-100"
              >
                <Home className="w-5 h-5" />
                <span>Dashboard</span>
              </Link>
              <Link
                to="/profile"
                className="flex items-center space-x-2 text-gray-700 hover:text-gray-900 px-3 py-2 rounded-md hover:bg-gray-100"
              >
                <User className="w-5 h-5" />
                <span>View Profile</span>
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
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center space-x-3 mb-6">
              <Lock className="w-8 h-8 text-blue-600" />
              <h2 className="text-2xl">Secure Content</h2>
            </div>
            
            <div className="space-y-4">
              <p className="text-gray-700">
                Hello, <span className="font-medium">{user?.firstName} {user?.lastName}</span>!
              </p>
              <p className="text-gray-600">
                This is a protected page that can only be accessed by authenticated users.
                If you weren't logged in, you would be redirected to the login page.
              </p>
              
              <div className="bg-blue-50 border border-blue-200 rounded-md p-4 mt-6">
                <h3 className="font-medium text-blue-900 mb-2">Protected Content</h3>
                <p className="text-sm text-blue-800">
                  This area contains sensitive information that requires authentication to view.
                  Only logged-in users like yourself can see this content.
                </p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
