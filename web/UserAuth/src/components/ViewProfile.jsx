import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Home, Shield, User, Mail, Calendar, UserCheck, Clock, Edit2, Save, X, Lock } from 'lucide-react';

export function ViewProfile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [editUsername, setEditUsername] = useState(user?.username || '');
  const [editEmail, setEditEmail] = useState(user?.email || '');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Never';
    return new Date(dateString).toLocaleString();
  };

  const handleSaveChanges = async () => {
    setError('');
    setSuccess('');
    setLoading(true);

    // Validate
    if (!editUsername.trim() || !editEmail.trim()) {
      setError('Username and email are required');
      setLoading(false);
      return;
    }

    if (newPassword && newPassword !== confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    if (newPassword && newPassword.length < 6) {
      setError('Password must be at least 6 characters');
      setLoading(false);
      return;
    }

    try {
      const API_URL = `http://${window.location.hostname}:8080/api`;
      const sessionToken = localStorage.getItem('sessionToken');

      const payload = {
        username: editUsername,
        email: editEmail,
        ...(newPassword && { password: newPassword })
      };

      const response = await fetch(`${API_URL}/user/profile/${user?.userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionToken}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        setSuccess('Profile updated successfully!');
        setNewPassword('');
        setConfirmPassword('');
        setIsEditing(false);
        // Refresh user data after 2 seconds
        setTimeout(() => {
          window.location.reload();
        }, 2000);
      } else {
        setError('Failed to update profile. Please try again.');
      }
    } catch (err) {
      setError('An error occurred while updating profile.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center space-x-2">
              <User className="w-5 h-5 text-blue-600" />
              <h1 className="text-xl">View Profile</h1>
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
          <div className="bg-white rounded-lg shadow overflow-hidden">
            {/* Profile Header */}
            <div className="bg-gradient-to-r from-blue-500 to-blue-600 px-6 py-12">
              <div className="flex items-center space-x-6">
                <div className="bg-white rounded-full p-4">
                  <User className="w-16 h-16 text-blue-600" />
                </div>
                <div className="text-white">
                  <h2 className="text-3xl mb-1">{user?.firstName} {user?.lastName}</h2>
                  <p className="text-blue-100">@{user?.username}</p>
                </div>
              </div>
            </div>

            {/* Profile Details */}
            <div className="px-6 py-8">
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-xl">Profile Information</h3>
                {!isEditing && (
                  <button
                    onClick={() => setIsEditing(true)}
                    className="flex items-center space-x-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                  >
                    <Edit2 className="w-4 h-4" />
                    <span>Edit Profile</span>
                  </button>
                )}
              </div>

              {error && (
                <div className="mb-4 p-4 bg-red-50 border border-red-200 rounded-md text-red-800">
                  {error}
                </div>
              )}

              {success && (
                <div className="mb-4 p-4 bg-green-50 border border-green-200 rounded-md text-green-800">
                  {success}
                </div>
              )}
              
              <div className="grid md:grid-cols-2 gap-6">
                <div className="flex items-start space-x-4">
                  {!isEditing && (
                    <div className="bg-blue-50 rounded-full p-3">
                      <User className="w-6 h-6 text-blue-600" />
                    </div>
                  )}
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">Username</p>
                    {isEditing ? (
                      <input
                        type="text"
                        value={editUsername}
                        onChange={(e) => setEditUsername(e.target.value)}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    ) : (
                      <p className="text-lg text-gray-900">@{user?.username}</p>
                    )}
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  {!isEditing && (
                    <div className="bg-green-50 rounded-full p-3">
                      <Mail className="w-6 h-6 text-green-600" />
                    </div>
                  )}
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">Email Address</p>
                    {isEditing ? (
                      <input
                        type="email"
                        value={editEmail}
                        onChange={(e) => setEditEmail(e.target.value)}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    ) : (
                      <p className="text-lg text-gray-900">{user?.email}</p>
                    )}
                  </div>
                </div>

                {isEditing && (
                  <>
                    <div className="flex items-start space-x-4">
                      <div className="flex-1">
                        <p className="text-sm text-gray-600">New Password (optional)</p>
                        <input
                          type="password"
                          value={newPassword}
                          onChange={(e) => setNewPassword(e.target.value)}
                          placeholder="Leave empty to keep current password"
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>

                    <div className="flex items-start space-x-4">
                      <div className="flex-1">
                        <p className="text-sm text-gray-600">Confirm Password</p>
                        <input
                          type="password"
                          value={confirmPassword}
                          onChange={(e) => setConfirmPassword(e.target.value)}
                          placeholder="Confirm new password"
                          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                      </div>
                    </div>
                  </>
                )}

                <div className="flex items-start space-x-4">
                  <div className="bg-purple-50 rounded-full p-3">
                    <User className="w-6 h-6 text-purple-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">First Name</p>
                    <p className="text-lg text-gray-900">{user?.firstName}</p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="bg-pink-50 rounded-full p-3">
                    <User className="w-6 h-6 text-pink-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">Last Name</p>
                    <p className="text-lg text-gray-900">{user?.lastName}</p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="bg-orange-50 rounded-full p-3">
                    <UserCheck className="w-6 h-6 text-orange-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">Role</p>
                    <p className="text-lg text-gray-900 capitalize">{user?.role || 'USER'}</p>
                  </div>
                </div>

                <div className="flex items-start space-x-4">
                  <div className="bg-teal-50 rounded-full p-3">
                    <Calendar className="w-6 h-6 text-teal-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-600">Account Status</p>
                    <p className="text-lg text-gray-900">
                      {user?.isActive ? 'Active' : 'Inactive'}
                    </p>
                  </div>
                </div>
              </div>

              {isEditing && (
                <div className="mt-6 flex space-x-4">
                  <button
                    onClick={handleSaveChanges}
                    disabled={loading}
                    className="flex items-center space-x-2 px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:bg-gray-400"
                  >
                    <Save className="w-4 h-4" />
                    <span>{loading ? 'Saving...' : 'Save Changes'}</span>
                  </button>
                  <button
                    onClick={() => {
                      setIsEditing(false);
                      setEditUsername(user?.username || '');
                      setEditEmail(user?.email || '');
                      setNewPassword('');
                      setConfirmPassword('');
                      setError('');
                    }}
                    disabled={loading}
                    className="flex items-center space-x-2 px-6 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 disabled:bg-gray-400"
                  >
                    <X className="w-4 h-4" />
                    <span>Cancel</span>
                  </button>
                </div>
              )}

              <div className="mt-8 pt-8 border-t border-gray-200">
                <h4 className="font-medium text-gray-900 mb-4">Account Details</h4>
                <div className="space-y-3 text-sm">
                  <div className="flex items-center space-x-2">
                    <Calendar className="w-4 h-4 text-gray-400" />
                    <span className="text-gray-600">Created:</span>
                    <span className="text-gray-900">{formatDate(user?.createdAt)}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Calendar className="w-4 h-4 text-gray-400" />
                    <span className="text-gray-600">Last Updated:</span>
                    <span className="text-gray-900">{formatDate(user?.updatedAt)}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Clock className="w-4 h-4 text-gray-400" />
                    <span className="text-gray-600">Last Login:</span>
                    <span className="text-gray-900">{formatDate(user?.lastLogin)}</span>
                  </div>
                </div>
              </div>

              <div className="mt-8 pt-8 border-t border-gray-200">
                <h4 className="font-medium text-gray-900 mb-4">Access Permissions</h4>
                <div className="space-y-2">
                  <div className="flex items-center space-x-2 text-sm">
                    <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                    <span className="text-gray-700">Dashboard Access</span>
                  </div>
                  <div className="flex items-center space-x-2 text-sm">
                    <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                    <span className="text-gray-700">View Profile</span>
                  </div>
                  <div className="flex items-center space-x-2 text-sm">
                    <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                    <span className="text-gray-700">Access Protected Pages</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
