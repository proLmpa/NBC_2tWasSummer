// src/components/Login.js

import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const handleLogin = async () => {
        try {
            const formdata = new FormData();
            // Call your API here to perform the login
            const response = await fetch('YOUR_API_ENDPOINT', {
                method: 'POST',
                body: formdata
            });

            if (response.ok) {
                // Handle successful login (e.g., set user state, redirect, etc.)
            } else {
                // Handle failed login (e.g., show error message)
            }
        } catch (error) {
            console.error('Error logging in:', error);
            // Handle error (e.g., show error message)
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
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
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

