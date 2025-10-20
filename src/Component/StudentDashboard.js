import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function StudentDashboard({ user }) {
  const navigate = useNavigate();
  const [previewUrl, setPreviewUrl] = useState(user.imageurl || "");
  const [uploadStatus, setUploadStatus] = useState("");
  const fileInputRef = useRef(null); // Reference for hidden file input

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login", { state: { message: "Logged out successfully!" } });
  };

  // When user clicks on avatar → trigger hidden file input
  const handleAvatarClick = () => {
    fileInputRef.current.click();
  };

  // When file selected
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const token = localStorage.getItem("jwttoken");
    const formData = new FormData();
    formData.append("file", file);

    setUploadStatus("⏳ Uploading...");

    try {
      const res = await axios.post(
        "http://localhost:8080/api/classes/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // assuming backend returns image URL
      const uploadedUrl = res.data.imageUrl || URL.createObjectURL(file);
      setPreviewUrl(uploadedUrl);
      setUploadStatus("✅ Profile picture updated successfully!");
    } catch (err) {
      console.error("Upload failed:", err);
      setUploadStatus("❌ Upload failed. Please try again.");
    }
  };

  return (
    <div className="dashboard-wrapper">
      {/* Sidebar */}
      <aside className="sidebar">
        <h2 className="brand">SkillCore</h2>

        <div className="sidebar-user">
          {/* Hidden File Input */}
          <input
            type="file"
            accept="image/*"
            ref={fileInputRef}
            style={{ display: "none" }}
            onChange={handleFileChange}
          />

          {/* Clickable Avatar */}
          <div
            className="avatar-wrapper"
            onClick={handleAvatarClick}
            style={{ cursor: "pointer", position: "relative" }}
          >
            <img
              src={
                previewUrl ||
                "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"
              }
              alt="User Avatar"
              style={{
                width: "120px",
                height: "120px",
                borderRadius: "50%",
                objectFit: "cover",
                border: "3px solid #ccc",
                transition: "0.3s",
              }}
            />
            <div
              style={{
                position: "absolute",
                bottom: "0",
                left: "0",
                right: "0",
                background: "rgba(0,0,0,0.5)",
                color: "white",
                fontSize: "12px",
                padding: "4px",
                textAlign: "center",
                borderBottomLeftRadius: "50%",
                borderBottomRightRadius: "50%",
              }}
            >
              Change
            </div>
          </div>

          {uploadStatus && (
            <p
              style={{
                color: uploadStatus.startsWith("✅") ? "green" : "red",
                fontSize: "13px",
                marginTop: "8px",
              }}
            >
              {uploadStatus}
            </p>
          )}

          <h3>{user.fullname}</h3>
          <p className="role">{user.role}</p>
        </div>

        <div className="sidebar-links">
          <button className="sidebar-btn active">Dashboard</button>
          <button
            className="sidebar-btn"
            onClick={() => navigate("/studentprofile")}
          >
            My Profile
          </button>
          <button className="sidebar-btn">Settings</button>
        </div>

        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </aside>

      {/* Main Dashboard */}
      <main className="dashboard-main">
        <header className="dashboard-header">
          <h1>Welcome, {user.fullname}! 👋</h1>
          <p>{user.email}</p>
          <p>📞 {user.phonenumber}</p>
        </header>

        {/* Enrolled Courses Section */}
        <section className="courses-section">
          <h2>📚 Enrolled Courses</h2>
          {user.totalenrolls && user.totalenrolls.length > 0 ? (
            <div className="courses-grid">
              {user.totalenrolls.map((enroll, index) => (
                <div className="course-card" key={index}>
                  <img
                    src={
                      enroll.course?.thumbnailUrl ||
                      "https://via.placeholder.com/300x180"
                    }
                    alt={enroll.course?.title}
                  />
                  <div className="course-content">
                    <h4>{enroll.course?.title}</h4>
                    <p>{enroll.course?.description}</p>
                    <p className="instructor">
                      👨‍🏫 {enroll.course?.instructor?.fullname}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="empty">You haven’t enrolled in any courses yet.</p>
          )}
        </section>
      </main>
    </div>
  );
}
