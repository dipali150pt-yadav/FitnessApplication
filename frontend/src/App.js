import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Dashboard from './pages/Dashboard';
import UserProfile from './pages/UserProfile';
import WorkoutRecommendations from './pages/WorkoutRecommendations';
import Activities from './pages/Activities';
import Register from './pages/Register';

function App() {
  const [user, setUser] = useState(null);

  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <div className="container">
            <div className="nav-brand">
              <h1>💪 FitAI</h1>
            </div>
            <div className="nav-links">
              <Link to="/">Dashboard</Link>
              <Link to="/workouts">AI Workouts</Link>
              <Link to="/activities">Activities</Link>
              <Link to="/profile">Profile</Link>
              {!user && <Link to="/register">Register</Link>}
            </div>
          </div>
        </nav>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard user={user} />} />
            <Route path="/register" element={<Register setUser={setUser} />} />
            <Route path="/profile" element={<UserProfile user={user} />} />
            <Route path="/workouts" element={<WorkoutRecommendations user={user} />} />
            <Route path="/activities" element={<Activities user={user} />} />
          </Routes>
        </main>

        <footer className="footer">
          <div className="container">
            <p>&copy; 2024 FitAI - AI-Powered Fitness Platform</p>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
