import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import StudentDashboard from "./Component/StudentDashboard";
import InstructorDashboard from "./Component/InstructorDashboard";

export default function Dashboard() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("jwttoken");
    const email = localStorage.getItem("email");

    if (!token) {
      navigate("/login");
      return;
    }

    axios
      .get(`http://localhost:8080/api/users/profile?email=${email}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setUser(res.data))
      .catch(() => alert("Access denied or invalid token"));
  });

  if (!user) return <div className="loading">Loading Dashboard...</div>;

  // ✅ Conditional routing based on role
  if (user.role === "Student") {
    return <StudentDashboard user={user} />;
  } else if (user.role === "Instructor") {
    return <InstructorDashboard user={user} />;
  } else {
    return <div>Unknown role. Please contact admin.</div>;
  }
}
