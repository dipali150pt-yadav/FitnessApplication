import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// User Service APIs
export const userAPI = {
  register: (userData) => api.post('/users/register', userData),
  getUser: (userId) => api.get(`/users/${userId}`),
  updateUser: (userId, userData) => api.put(`/users/${userId}`, userData),
  getAllUsers: () => api.get('/users'),
};

// Workout Service APIs
export const workoutAPI = {
  getRecommendation: (requestData) => api.post('/workouts/recommend', requestData),
  getUserWorkouts: (userId) => api.get(`/workouts/user/${userId}`),
  createWorkout: (workoutData) => api.post('/workouts', workoutData),
  getWorkout: (workoutId) => api.get(`/workouts/${workoutId}`),
  deleteWorkout: (workoutId) => api.delete(`/workouts/${workoutId}`),
};

// Activity Service APIs
export const activityAPI = {
  createActivity: (activityData) => api.post('/activities', activityData),
  getUserActivities: (userId) => api.get(`/activities/user/${userId}`),
  getUserStats: (userId) => api.get(`/activities/user/${userId}/stats`),
  getActivity: (activityId) => api.get(`/activities/${activityId}`),
  deleteActivity: (activityId) => api.delete(`/activities/${activityId}`),
  getActivitiesByDateRange: (userId, startDate, endDate) =>
    api.get(`/activities/user/${userId}/range`, {
      params: { startDate, endDate },
    }),
};

export default api;
