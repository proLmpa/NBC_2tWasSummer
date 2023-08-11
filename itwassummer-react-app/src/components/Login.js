// src/components/Login.js

import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();
  const handleLogin = async () => {
    try {
      console.log(email, password)
      // Call your API here to perform the login
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({email, password})
      });

      if (response.ok) {
        console.log(response)
      } else {
        console.log(response)
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
  //
  //
  // const formRef = useRef();
  // const [cookies, setCookie] = useCookies(['id']); // 쿠키 훅
  //
  //
  // const login = (e) => {
  //   e.preventDefault();
  //   axios
  //     .post('/users/login', { // 로그인 요청
  //       id: formRef.current.id.value,
  //       password: formRef.current.passWord.value,
  //     })
  //     .then((res) => {
  //       setCookie('id', res.data.token);// 쿠키에 토큰 저장
  //     });
  // };
  //
  // return (
  //   <form ref={formRef} onSubmit={login}>
  //     <input type="text" name="id" placeholder="id" required />
  //     <input type="password" name="passWord" placeholder="passWord" required />
  //     <input type="submit"></input>
  //   </form>
  // );
};

export default Login;

