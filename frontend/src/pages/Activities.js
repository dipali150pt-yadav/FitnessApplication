import React, { useState, useEffect } from 'react';
import { activityAPI } from '../services/api';

function Activities({ user }) {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [formData, setFormData] = useState({
    name: '',
    activityType: 'RUNNING',
    durationMinutes: '',
    caloriesBurned: '',
    distanceKm: '',
    intensity: 'MODERATE',
    notes: '',
    activityDate: new Date().toISOString().slice(0, 16),
  });

  useEffect(() => {
    if (user && user.id) {
      loadActivities();
    }
  }, [user]);

  const loadActivities = async () => {
    try {
      setLoading(true);
      const response = await activityAPI.getUserActivities(user.id);
      setActivities(response.data);
    } catch (error) {
      console.error('Error loading activities:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!user || !user.id) {
      setError('Please register or log in first');
      return;
    }

    try {
      const activityData = {
        userId: user.id,
        ...formData,
        durationMinutes: parseInt(formData.durationMinutes),
        caloriesBurned: formData.caloriesBurned ? parseInt(formData.caloriesBurned) : null,
        distanceKm: formData.distanceKm ? parseFloat(formData.distanceKm) : null,
      };

      await activityAPI.createActivity(activityData);
      setSuccess('Activity logged successfully!');
      setShowForm(false);
      loadActivities();

      // Reset form
      setFormData({
        name: '',
        activityType: 'RUNNING',
        durationMinutes: '',
        caloriesBurned: '',
        distanceKm: '',
        intensity: 'MODERATE',
        notes: '',
        activityDate: new Date().toISOString().slice(0, 16),
      });
    } catch (err) {
      setError('Failed to log activity. Please try again.');
      console.error('Error:', err);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>Activity Log</h1>
        <p>Track your fitness activities and progress</p>
      </div>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      <div className="card">
        <button
          onClick={() => setShowForm(!showForm)}
          className="btn btn-primary"
          style={{ marginBottom: '20px' }}
        >
          {showForm ? '❌ Cancel' : '➕ Log New Activity'}
        </button>

        {showForm && (
          <form onSubmit={handleSubmit}>
            <div className="grid grid-2">
              <div className="input-group">
                <label>Activity Name *</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  placeholder="E.g., Morning Run"
                />
              </div>

              <div className="input-group">
                <label>Activity Type *</label>
                <select name="activityType" value={formData.activityType} onChange={handleChange} required>
                  <option value="RUNNING">Running</option>
                  <option value="WALKING">Walking</option>
                  <option value="CYCLING">Cycling</option>
                  <option value="SWIMMING">Swimming</option>
                  <option value="WEIGHTLIFTING">Weightlifting</option>
                  <option value="YOGA">Yoga</option>
                  <option value="HIKING">Hiking</option>
                  <option value="DANCING">Dancing</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="input-group">
                <label>Duration (minutes) *</label>
                <input
                  type="number"
                  name="durationMinutes"
                  value={formData.durationMinutes}
                  onChange={handleChange}
                  required
                  min="1"
                />
              </div>

              <div className="input-group">
                <label>Intensity</label>
                <select name="intensity" value={formData.intensity} onChange={handleChange}>
                  <option value="LOW">Low</option>
                  <option value="MODERATE">Moderate</option>
                  <option value="HIGH">High</option>
                  <option value="EXTREME">Extreme</option>
                </select>
              </div>

              <div className="input-group">
                <label>Calories Burned (optional)</label>
                <input
                  type="number"
                  name="caloriesBurned"
                  value={formData.caloriesBurned}
                  onChange={handleChange}
                  min="1"
                />
              </div>

              <div className="input-group">
                <label>Distance (km, optional)</label>
                <input
                  type="number"
                  name="distanceKm"
                  value={formData.distanceKm}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                />
              </div>
            </div>

            <div className="input-group">
              <label>Activity Date & Time *</label>
              <input
                type="datetime-local"
                name="activityDate"
                value={formData.activityDate}
                onChange={handleChange}
                required
              />
            </div>

            <div className="input-group">
              <label>Notes</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleChange}
                rows="3"
                placeholder="How did you feel? Any observations?"
              />
            </div>

            <button type="submit" className="btn btn-secondary" style={{ width: '100%' }}>
              💪 Log Activity
            </button>
          </form>
        )}
      </div>

      <div className="card">
        <h2>Recent Activities</h2>
        {loading ? (
          <div className="loading">Loading activities...</div>
        ) : activities.length > 0 ? (
          <ul className="activity-list">
            {activities.map((activity) => (
              <li key={activity.id} className="activity-item">
                <div className="activity-info">
                  <h4>{activity.name}</h4>
                  <p>
                    {activity.activityType} • {activity.intensity} • {formatDate(activity.activityDate)}
                  </p>
                </div>
                <div className="activity-stats">
                  <div>
                    <strong>{activity.durationMinutes}</strong> min
                  </div>
                  <div>
                    <strong>{activity.caloriesBurned || 0}</strong> cal
                  </div>
                  {activity.distanceKm && (
                    <div>
                      <strong>{activity.distanceKm}</strong> km
                    </div>
                  )}
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p style={{ textAlign: 'center', color: '#718096', padding: '40px' }}>
            No activities logged yet. Start by logging your first activity!
          </p>
        )}
      </div>
    </div>
  );
}

export default Activities;
