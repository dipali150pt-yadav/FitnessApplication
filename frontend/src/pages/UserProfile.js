import React from 'react';

function UserProfile({ user }) {
  if (!user) {
    return (
      <div className="container">
        <div className="page-header">
          <h1>User Profile</h1>
          <p>View and manage your profile</p>
        </div>
        <div className="card">
          <p>Please register or log in to view your profile.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="page-header">
        <h1>User Profile</h1>
        <p>Your personal information and fitness goals</p>
      </div>

      <div className="card">
        <h2>Personal Information</h2>
        <div className="grid grid-2" style={{ marginTop: '20px' }}>
          <div>
            <strong>Name:</strong>
            <p>{user.firstName} {user.lastName}</p>
          </div>
          <div>
            <strong>Email:</strong>
            <p>{user.email}</p>
          </div>
          {user.age && (
            <div>
              <strong>Age:</strong>
              <p>{user.age} years</p>
            </div>
          )}
          {user.gender && (
            <div>
              <strong>Gender:</strong>
              <p>{user.gender}</p>
            </div>
          )}
          {user.weight && (
            <div>
              <strong>Weight:</strong>
              <p>{user.weight} kg</p>
            </div>
          )}
          {user.height && (
            <div>
              <strong>Height:</strong>
              <p>{user.height} cm</p>
            </div>
          )}
        </div>
      </div>

      <div className="card">
        <h2>Fitness Profile</h2>
        <div className="grid grid-2" style={{ marginTop: '20px' }}>
          {user.fitnessLevel && (
            <div>
              <strong>Fitness Level:</strong>
              <p>
                <span className={`workout-badge badge-${user.fitnessLevel.toLowerCase()}`}>
                  {user.fitnessLevel}
                </span>
              </p>
            </div>
          )}
          {user.role && (
            <div>
              <strong>Role:</strong>
              <p>{user.role}</p>
            </div>
          )}
        </div>
        {user.fitnessGoal && (
          <div style={{ marginTop: '20px' }}>
            <strong>Fitness Goal:</strong>
            <p>{user.fitnessGoal}</p>
          </div>
        )}
      </div>

      <div className="card">
        <h2>Account Details</h2>
        <div style={{ marginTop: '20px' }}>
          <strong>Member Since:</strong>
          <p>{new Date(user.createdAt).toLocaleDateString()}</p>
        </div>
        {user.updatedAt && (
          <div style={{ marginTop: '12px' }}>
            <strong>Last Updated:</strong>
            <p>{new Date(user.updatedAt).toLocaleDateString()}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default UserProfile;
