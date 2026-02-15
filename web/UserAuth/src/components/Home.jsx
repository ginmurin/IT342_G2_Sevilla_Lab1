import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserCircle, LogIn, UserPlus } from 'lucide-react';
import { useEffect } from 'react';

export function Home() {
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // If user is already logged in, redirect to dashboard
    if (user) {
      navigate('/dashboard');
    }
  }, [user, navigate]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center px-4">
      <div className="max-w-4xl w-full">
        <div className="text-center mb-12">
          <div className="flex justify-center mb-6">
            <UserCircle className="w-20 h-20 text-blue-600" />
          </div>
          <h1 className="text-4xl mb-4">Welcome Guest</h1>
          <p className="text-xl text-gray-600">
            Please login or register to access the application
          </p>
        </div>

        <div className="grid md:grid-cols-2 gap-6 max-w-2xl mx-auto">
          <Link
            to="/login"
            className="bg-white rounded-lg shadow-lg p-8 hover:shadow-xl transition-shadow text-center group"
          >
            <LogIn className="w-12 h-12 text-blue-600 mx-auto mb-4 group-hover:scale-110 transition-transform" />
            <h2 className="text-2xl mb-2">Login</h2>
            <p className="text-gray-600">
              Already have an account? Sign in to continue
            </p>
          </Link>

          <Link
            to="/register"
            className="bg-white rounded-lg shadow-lg p-8 hover:shadow-xl transition-shadow text-center group"
          >
            <UserPlus className="w-12 h-12 text-green-600 mx-auto mb-4 group-hover:scale-110 transition-transform" />
            <h2 className="text-2xl mb-2">Register</h2>
            <p className="text-gray-600">
              New user? Create an account to get started
            </p>
          </Link>
        </div>

        <div className="mt-8 text-center">
          <p className="text-gray-600 mb-2">Demo Account Credentials:</p>
          <div className="inline-block bg-white rounded-lg shadow px-6 py-3">
            <p className="text-sm">
              <span className="font-medium">Username:</span> demouser
            </p>
            <p className="text-sm">
              <span className="font-medium">Password:</span> demo123
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

