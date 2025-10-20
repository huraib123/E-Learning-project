import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";


export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8080/api/auth/login", {
        email,
        password,
      });
      if (res.data.token) {
        localStorage.setItem("jwttoken", res.data.token);
        localStorage.setItem("email", res.data.email);
        navigate("/afterlogin");
      } else {
        setError("Invalid credentials");
      }
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <div className="modern-login-page">
      <div className="background-shape"></div>
      <div className="background-shape2"></div>

      <div className="login-card">
        <div className="login-header">
          <img
            src="https://cdn-icons-png.flaticon.com/512/906/906343.png"
            alt="logo"
            className="logo"
          />
          <h2>Welcome Back 👋</h2>
          <p>Log in to continue your journey</p>
        </div>

        {error && <p className="error-text">{error}</p>}

        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="login-btn">
            Sign In
          </button>
        </form>

        <p className="signup-link">
  Don’t have an account? <Link to="/signup">Create one</Link>
</p>

      </div>
    </div>
  );
}
