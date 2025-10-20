import React from "react";
import { useNavigate } from "react-router-dom";

export default function InstructorDashboard({ user }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login", { state: { message: "Logged out successfully!" } });
  };

  return (
    <div className="dashboard-wrapper">
      {/* Sidebar */}
      <aside className="sidebar">
        <h2 className="brand">SkillCore</h2>
        <div className="sidebar-user">
          <img
            src={
              user.imageurl ||
              "https://cdn-icons-png.flaticon.com/512/3135/3135715.png"
            }
            alt="User Avatar"
          />
          <h3>{user.fullname}</h3>
          <p className="role">{user.role}</p>
        </div>

        <div className="sidebar-links">
          <button className="sidebar-btn active">Dashboard</button>
          <button className="sidebar-btn">My Profile</button>
          <button className="sidebar-btn">Settings</button>
        </div>

        <button className="logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </aside>

      {/* Main Content */}
      <main className="dashboard-main">
        <header className="dashboard-header">
          <h1>Welcome, {user.fullname}! 👋</h1>
          <p>{user.email}</p>
          <p>📞 {user.phonenumber}</p>
        </header>

        <section className="courses-section">
          <h2>🎓 Courses You Teach</h2>
          {user.totalcourses && user.totalcourses.length > 0 ? (
            <div className="courses-grid">
              {user.totalcourses.map((course) => (
                <div className="course-card" key={course.id}>
                  <img
                    src={
                      course.thumbnailUrl ||
                      "https://via.placeholder.com/300x180"
                    }
                    alt={course.title}
                  />
                  <div className="course-content">
                    <h4>{course.title}</h4>
                    <p>{course.description}</p>
                    <p className="students">
                      👥 Students Enrolled: {course.totalStudents || 0}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="empty">You haven’t created any courses yet.</p>
          )}
        </section>

        <div className="add-course">
          <button
            className="add-course-btn"
            onClick={() => navigate("/Fileupload")}
          >
            ➕ Add New Course
          </button>
        </div>
      </main>
    </div>
  );
}
