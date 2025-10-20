import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";


export default function SignUp() {
  const [data, setData] = useState({
    fullname: "",
    email: "",
    password: "",
    phonenumber: "",
    role: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setData({
      ...data,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/api/auth/signup", data);

      if (response.status === 200 || response.status === 201) {
        alert("Sign up successful! OTP sent to your email.");
        navigate("/otppage");
      }
    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data.message || "Sign-up failed.");
      } else {
        setError("Server error. Please try again.");
      }
    }

    // Reset fields
    setData({
      fullname: "",
      email: "",
      password: "",
      phonenumber: "",
      role: "",
    });
  };

  return (
    <div className="signup-container">
      <div className="signup-card">
        <div className="signup-left">
          <h2 className="logo">📘 E-Learning</h2>
          <h1 className="signup-title">Sign Up</h1>

          <form onSubmit={handleSubmit} className="signup-form">
            <input
              type="text"
              name="fullname"
              placeholder="Enter your name"
              value={data.fullname}
              onChange={handleChange}
              required
            />

            <input
              type="email"
              name="email"
              placeholder="Email"
              value={data.email}
              onChange={handleChange}
              required
            />

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={data.password}
              onChange={handleChange}
              required
            />

            <input
              type="text"
              name="phonenumber"
              placeholder="Phone Number"
              value={data.phonenumber}
              onChange={handleChange}
              required
            />

            <input
              type="text"
              name="role"
              placeholder="Role"
              value={data.role}
              onChange={handleChange}
              required
            />

            <button type="submit" className="signup-btn">
              Sign Up
            </button>

            {error && <p className="error-message">{error}</p>}

            <p className="login-text">
              Already have an account? <Link to="/login">Log In</Link>
            </p>
          </form>
        </div>

        <div className="signup-right">
          <img
            src="https://cdn-icons-png.flaticon.com/512/4140/4140048.png"
            alt="E-learning signup illustration"
            className="signup-img"
          />
        </div>
      </div>
    </div>
  );
}
