// src/components/Login.js

import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {useRecoilState, useRecoilValue} from "recoil";
import {isLoginSelector, TokenAtom} from "../recoil/TokenAtom";


const Login = () => {
  //기본 ID/PW 변수 선언
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // 토큰값을 recoil에 담아주는 코드
  const [getAccessToken, setAccessToken] = useRecoilState(TokenAtom)

  // navigate 선언
  const navigate = useNavigate();

  // 셀렉터 가져오기
  const isLogin = useRecoilValue(isLoginSelector)

/////////////////////////////////

  useEffect(() => {
    if (isLogin) {
      navigate('/board')
    }
  }, [])


  // Login 버튼 눌렀을 때 동작
  const handleLogin = async () => {

    try {
      const response = await axios.post('http://localhost:8080/api/users/login', {email, password});
      const token = response.headers['authorization'];

      // recoil에 토큰 값 담기
      setAccessToken(token)
      console.log('getToken값 잘 저장되어있나 ', getAccessToken)

      // 쿠키에 값 담기 키는 jwtToken으로 설정하고 있다
      document.cookie = `Authorization=${token}; path=/`;

    } catch (e) {
      alert(e.message)
    }
    // 이후 이동
    // navigate('/board')
  };

  const handleSignupClick = () => {
    navigate('/signup') // v6
  };
  const handleHomeClick = () => {
    navigate('/') // v6
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
      <button onClick={handleHomeClick}>Go Home</button>

      <p>현재 Token : {getAccessToken}</p>
    </div>
  );
};

export default Login;

