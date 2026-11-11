import React, { useState } from 'react';
import { workoutAPI } from '../services/api';

function WorkoutRecommendations({ user }) {
  const [loading, setLoading] = useState(false);
  const [workout, setWorkout] = useState(null);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    durationMinutes: 30,
    preferredWorkoutType: '',
    difficulty: '',
    equipment: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleGetRecommendation = async (e) => {
    e.preventDefault();
    
    if (!user || !user.id) {
      setError('Please register or log in first');
      return;
    }

    setLoading(true);
    setError('');
    setWorkout(null);

    try {
      const requestData = {
        userId: user.id,
        ...formData,
      };

      const response = await workoutAPI.getRecommendation(requestData);
      setWorkout(response.data);
    } catch (err) {
      setError('Failed to get workout recommendation. Please try again.');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  const parseExercises = (exercisesString) => {
    try {
      return JSON.parse(exercisesString);
    } catch {
      return [];
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1>AI Workout Recommendations</h1>
        <p>Get personalized workout plans powered by AI</p>
      </div>

      <div className="grid grid-2">
        <div className="card">
          <h2>Customize Your Workout</h2>
          <form onSubmit={handleGetRecommendation}>
            <div className="input-group">
              <label>Duration (minutes)</label>
              <input
                type="number"
                name="durationMinutes"
                value={formData.durationMinutes}
                onChange={handleChange}
                min="10"
                max="180"
              />
            </div>

            <div className="input-group">
              <label>Workout Type</label>
              <select name="preferredWorkoutType" value={formData.preferredWorkoutType} onChange={handleChange}>
                <option value="">Any</option>
                <option value="CARDIO">Cardio</option>
                <option value="STRENGTH">Strength</option>
                <option value="FLEXIBILITY">Flexibility</option>
                <option value="HIIT">HIIT</option>
                <option value="YOGA">Yoga</option>
                <option value="MIXED">Mixed</option>
              </select>
            </div>

            <div className="input-group">
              <label>Difficulty</label>
              <select name="difficulty" value={formData.difficulty} onChange={handleChange}>
                <option value="">Auto (Based on profile)</option>
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
                <option value="EXPERT">Expert</option>
              </select>
            </div>

            <div className="input-group">
              <label>Available Equipment</label>
              <input
                type="text"
                name="equipment"
                value={formData.equipment}
                onChange={handleChange}
                placeholder="E.g., dumbbells, resistance bands, none"
              />
            </div>

            <button type="submit" className="btn btn-primary" disabled={loading} style={{ width: '100%' }}>
              {loading ? 'Generating...' : '✨ Get AI Recommendation'}
            </button>
          </form>
        </div>

        <div>
          {error && <div className="error">{error}</div>}

          {loading && (
            <div className="card">
              <div className="loading">🤖 AI is creating your personalized workout...</div>
            </div>
          )}

          {workout && (
            <div className="card workout-card">
              <h2>{workout.name}</h2>
              <div style={{ marginBottom: '16px' }}>
                <span className={`workout-badge badge-${workout.difficulty?.toLowerCase()}`}>
                  {workout.difficulty}
                </span>
                <span className="workout-badge" style={{ background: '#e6fffa', color: '#234e52' }}>
                  {workout.type}
                </span>
                {workout.aiGenerated && (
                  <span className="workout-badge" style={{ background: '#fef5e7', color: '#7d4e00' }}>
                    🤖 AI Generated
                  </span>
                )}
              </div>

              <p style={{ marginBottom: '16px', color: '#4a5568' }}>{workout.description}</p>

              <div style={{ display: 'flex', gap: '20px', marginBottom: '20px' }}>
                <div>
                  <strong>Duration:</strong> {workout.durationMinutes} min
                </div>
                <div>
                  <strong>Calories:</strong> ~{workout.estimatedCalories} kcal
                </div>
              </div>

              <h3 style={{ marginBottom: '12px' }}>Exercises:</h3>
              <ul style={{ listStyle: 'none' }}>
                {parseExercises(workout.exercises).map((exercise, index) => (
                  <li
                    key={index}
                    style={{
                      padding: '12px',
                      background: '#f7fafc',
                      marginBottom: '8px',
                      borderRadius: '8px',
                    }}
                  >
                    <strong>{exercise.name}</strong>
                    <div style={{ fontSize: '14px', color: '#718096', marginTop: '4px' }}>
                      {exercise.sets && <span>Sets: {exercise.sets} </span>}
                      {exercise.reps && <span>Reps: {exercise.reps} </span>}
                      {exercise.duration && <span>Duration: {exercise.duration}</span>}
                    </div>
                    {exercise.instructions && (
                      <p style={{ fontSize: '14px', marginTop: '8px', color: '#4a5568' }}>
                        {exercise.instructions}
                      </p>
                    )}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default WorkoutRecommendations;
