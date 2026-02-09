import { Link, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { UserCircle, LogIn, UserPlus } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export function Home() {
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      navigate('/dashboard');
    }
  }, [user, navigate]);

  return (
    <div className="min-h-screen bg-slate-200 px-4 py-12">
      <div className="mx-auto w-full max-w-3xl text-center">
        <div className="mb-5 flex justify-center">
          <UserCircle className="h-20 w-20 text-blue-600" strokeWidth={1.75} />
        </div>

        <h1 className="text-5xl font-medium text-slate-900">Welcome Guest</h1>
        <p className="mt-3 text-base text-slate-600">Please login or register to access the application</p>

        <div className="mt-12 grid gap-6 md:grid-cols-2">
          <Link
            to="/login"
            className="rounded-2xl bg-white p-8 shadow-sm ring-1 ring-slate-200 transition hover:-translate-y-0.5 hover:shadow-md"
          >
            <LogIn className="mx-auto h-12 w-12 text-blue-600" strokeWidth={2} />
            <h2 className="mt-4 text-4xl font-medium text-slate-900">Login</h2>
            <p className="mt-2 text-base text-slate-600">Already have an account? Sign in to continue</p>
          </Link>

          <Link
            to="/register"
            className="rounded-2xl bg-white p-8 shadow-sm ring-1 ring-slate-200 transition hover:-translate-y-0.5 hover:shadow-md"
          >
            <UserPlus className="mx-auto h-12 w-12 text-emerald-600" strokeWidth={2} />
            <h2 className="mt-4 text-4xl font-medium text-slate-900">Register</h2>
            <p className="mt-2 text-base text-slate-600">New user? Create an account to get started</p>
          </Link>
        </div>
      </div>
    </div>
  );
}

