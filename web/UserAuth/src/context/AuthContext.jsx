import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(undefined);

// Dynamically get API URL based on current host
const getApiUrl = () => {
  const hostname = window.location.hostname;
  const port = 8080;
  return `http://${hostname}:${port}/api`;
};

const API_URL = getApiUrl();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Restore logged-in user and session token
    const storedUser = localStorage.getItem('user');
    const storedToken = localStorage.getItem('sessionToken');
    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const register = async (email, password, name) => {
    try {
      setError(null);
      const [firstName, lastName] = name.includes(' ') 
        ? name.split(' ', 2) 
        : [name, ''];
      
      const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: email.split('@')[0],
          email,
          password,
          firstName,
          lastName: lastName || 'User'
        })
      });

      const data = await response.json();
      
      if (response.ok) {
        return true;
      } else {
        setError(data.message || 'Registration failed');
        return false;
      }
    } catch (err) {
      setError('Failed to connect to server. Make sure backend is running on http://localhost:8080');
      return false;
    }
  };

  const login = async (email, password) => {
    try {
      setError(null);
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: email.split('@')[0],
          password
        })
      });

      const data = await response.json();

      if (response.ok && data.isAuthenticated) {
        const userData = {
          userId: data.userId,
          username: data.username,
          email: data.email,
          firstName: data.firstName,
          lastName: data.lastName
        };
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('sessionToken', data.sessionToken);
        return true;
      } else {
        setError(data.message || 'Invalid credentials');
        return false;
      }
    } catch (err) {
      setError('Failed to connect to server. Make sure backend is running on http://localhost:8080');
      return false;
    }
  };

  const logout = async () => {
    try {
      const token = localStorage.getItem('sessionToken');
      if (token) {
        await fetch(`${API_URL}/auth/logout`, {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${token}` }
        });
      }
    } catch (err) {
      console.error('Logout error:', err);
    }
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('sessionToken');
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, error }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
