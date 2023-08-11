// src/components/Login.js

import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();
  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/users/login', {email, password});
      const token = response.headers['authorization'];

      // Store the token in a cookie
      document.cookie = `jwtToken=${token}; path=/`;

      navigate('/board')

    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  const handleSignupClick = () => {
    navigate('/signup') // v6
  };


  return (
    <div>
      <h2>Login</h2>
      <input
        type="text"
        placeholder="Email"
        value={email}
        onChange={(e) => {
          setEmail(e.target.value)
          console.log(email)
        }}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>Login</button>
      <button onClick={handleSignupClick}>Go to Signup</button>
    </div>
  );
};

export default Login;

