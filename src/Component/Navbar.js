import React from 'react'
import { Link } from "react-router-dom";


export default function Navbar() {
  return (
    <div>
        <nav className="navbar">
      <div className="navbar-logo">
        <Link to="/">E-Learning</Link>
      </div>

      <ul className="navbar-links">
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/courses">Courses</Link>
        </li>
        <li>
          <Link to="/profile">Profile</Link>
        </li>
        <li>
          <Link to="/login">Login</Link>
        </li>
        <li>
          <Link to="/signup" className="signup-btn">
            Signup
          </Link>
        </li>
      </ul>
    </nav>
        
     
      
    </div>
  )
}
