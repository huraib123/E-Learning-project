import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";


export default function OtpVerify() {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const navigate = useNavigate();

  const handleVerify = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `http://localhost:8080/api/auth/verify?email=${email}&otp=${otp}`
      );

      if (response.status === 200 || response.status === 201) {
        alert("✅ Email verified successfully! Please proceed to login.");
        navigate("/login");
      }
    } catch (error) {
      if (error.response) {
        alert("❌ Couldn't verify OTP. Please try again.");
      } else {
        alert("⚠️ Server error. Try again later.");
      }
    }
  };

  return (
    <div className="otp-page">
      <div className="otp-card">
        {/* Left Section - Form */}
        <div className="otp-left">
          <h2 className="otp-title">Verify Your Account</h2>
          <p className="otp-subtitle">
            Please enter the OTP sent to your registered email address.
          </p>

          <form onSubmit={handleVerify} className="otp-form">
            <label>Email Address</label>
            <input
              onChange={(e) => setEmail(e.target.value)}
              name="email"
              type="email"
              placeholder="Enter your Email"
              className="otp-input"
              value={email}
              required
            />

            <label>One-Time Password</label>
            <input
              onChange={(e) => setOtp(e.target.value)}
              name="otp"
              type="text"
              placeholder="Enter OTP"
              className="otp-input"
              value={otp}
              required
            />

            <button type="submit" className="verify-btn">
              Verify OTP
            </button>

            <p className="resend">
              Didn’t receive the OTP? <a href="#">Resend Code</a>
            </p>
          </form>
        </div>

        {/* Right Section - Illustration */}
        <div className="otp-right">
          <img
            src="https://cdn-icons-png.flaticon.com/512/5957/5957610.png"
            alt="Email Verification"
            className="otp-illustration"
          />
        </div>
      </div>
    </div>
  );
}
