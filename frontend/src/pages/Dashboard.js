import React, { useState, useEffect } from 'react';
import { activityAPI } from '../services/api';

function Dashboard({ user }) {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user && user.id) {
      loadStats();
    }
  }, [user]);

  const loadStats = async () => {
    try {
      setLoading(true);
      const response = await activityAPI.getUserStats(user.id);
      setStats(response.data);
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  if (!user) {
    return (
      <div className="container">
        <div className="page-header">
          <h1>Welcome to FitAI</h1>
          <p>Your AI-Powered Fitness Companion</p>
        </div>
        <div className="card">
          <h2>Get Started</h2>
          <p>Please register or log in to access your personalized fitness dashboard.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header">
        <h1>Welcome back, {user.firstName}!</h1>
        <p>Your Fitness Dashboard</p>
      </div>

      {loading ? (
        <div className="loading">Loading your stats...</div>
      ) : stats ? (
        <div className="grid grid-3">
          <div className="stat-card">
            <h3>{stats.totalActivities || 0}</h3>
            <p>Total Activities</p>
          </div>
          <div className="stat-card">
            <h3>{stats.totalCaloriesBurned || 0}</h3>
            <p>Calories Burned</p>
          </div>
          <div className="stat-card">
            <h3>{stats.totalDurationMinutes || 0}</h3>
            <p>Minutes Exercised</p>
          </div>
          <div className="stat-card">
            <h3>{stats.totalDistanceKm?.toFixed(2) || 0}</h3>
            <p>Kilometers Covered</p>
          </div>
          <div className="stat-card">
            <h3>{stats.averageActivityDuration?.toFixed(0) || 0}</h3>
            <p>Avg. Duration (min)</p>
          </div>
          <div className="stat-card">
            <h3>{stats.averageCaloriesPerActivity || 0}</h3>
            <p>Avg. Calories/Activity</p>
          </div>
        </div>
      ) : (
        <div className="card">
          <h2>Start Your Fitness Journey</h2>
          <p>No activity data yet. Start by getting AI workout recommendations or log your first activity!</p>
        </div>
      )}

      <div className="card" style={{ marginTop: '40px' }}>
        <h2>Quick Actions</h2>
        <div className="grid grid-2" style={{ marginTop: '20px' }}>
          <a href="/workouts" className="btn btn-primary" style={{ textAlign: 'center', textDecoration: 'none' }}>
            Get AI Workout Recommendation
          </a>
          <a href="/activities" className="btn btn-secondary" style={{ textAlign: 'center', textDecoration: 'none' }}>
            Log Activity
          </a>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
