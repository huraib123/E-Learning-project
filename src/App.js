import './App.css';
import Navbar from './Component/Navbar';
import Home from './Component/Home';
import Login from './Component/Login';
import Sign from './Component/Sign';
import Coursepage from './Component/Coursepage';
import { Routes, Route, useNavigate } from "react-router-dom";
import Profilepage from './Component/Profilepage';
import OtpVerify from './Component/OtpVerify';
import Dashboard from './Dashboard';
import { useState, useEffect } from 'react';
import Fileupload from './Component/Fileupload';
import StudentProfile from './Component/StudentProfile';
import axios from 'axios';

function App() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("jwttoken");
    const email = localStorage.getItem("email");

     const currentPath = window.location.pathname;
  const protectedRoutes = ["/afterlogin", "/studentprofile", "/profile"];

  if (protectedRoutes.includes(currentPath)) {
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
  }
}, [navigate]);

  const [loggedin, setLoggedin] = useState(!!localStorage.getItem("jwttoken"));

  return (
    <div className="App">
      {/* <Navbar /> */}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="login" element={<Login />} />
        <Route path="/afterlogin" element={loggedin ? <Dashboard /> : <Login loginsuccess={() => setLoggedin(true)} />} />
        <Route path="/signup" element={<Sign />} />
        {/* <Route path="/signup" element={<Home/>}/> */}
        <Route path="/courses" element={<Coursepage />} />
        <Route path="/profile" element={<Profilepage />} />
        <Route path="/otppage" element={<OtpVerify />} />
        <Route path="/Fileupload" element={<Fileupload />} />
        <Route path="/studentprofile" element={<StudentProfile user={user} />} />
      </Routes>
    </div>
  );
}

export default App;
