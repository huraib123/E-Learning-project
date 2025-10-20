import React, { useState } from "react";
import axios from "axios";

export default function StudentProfile({ user }) {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = "/login";
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("jwttoken");

    if (!oldPassword || !newPassword) {
      setMessage("Please fill both fields");
      return;
    }

    try {
      const res = await axios.post(
        "http://localhost:8080/api/auth/change",
        {
          oldPassword,
          newPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setMessage(res.data);
      setOldPassword("");
      setNewPassword("");
    } catch (err) {
      if (err.response) {
        setMessage(err.response.data);
      } else {
        setMessage("Error changing password");
      }
    }
  };

  return (
    <div className="profile-page">
      <h1>My Profile</h1>

      {/* Profile Info */}
      <div className="profile-card">
        {/* <img
          src={
            user.imageurl ||
            "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"
          }
          alt="did not found"
          className="profile-pic"
        /> */}
        <div className="profile-info">
          <p><strong>Full Name:</strong> {user.fullname}</p>
          <p><strong>Email:</strong> {user.email}</p>
          <p><strong>Phone:</strong> {user.phonenumber || "Not Provided"}</p>
          <p><strong>Role:</strong> {user.role}</p>
        </div>
      </div>

      {/* Actions */}
      <div className="profile-actions">
        <button onClick={handleLogout}>🚪 Logout</button>
      </div>

      {/* Change Password Form */}
      <div className="change-password">
        <h2>🔒 Change Password</h2>
        <form onSubmit={handleChangePassword}>
          <div>
            <label>Old Password:</label>
            <input
              type="password"
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              required
            />
          </div>
          <div>
            <label>New Password:</label>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit">Change Password</button>
        </form>
        {message && <p className="message">{message}</p>}
      </div>
    </div>
  );
}
