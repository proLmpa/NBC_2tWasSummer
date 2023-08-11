import React from "react";
import {useNavigate} from "react-router-dom";

const Home = () => {

  const navigate = useNavigate()
  const handleLogin = () => {
    navigate('/login') // v6
  };

  const handleSignupClick = () => {
    navigate('/signup') // v6
  };

  return (
    <div>
      <button onClick={handleLogin}> 로그인하기</button>
      <button onClick={handleSignupClick}> 회원가입하기</button>
    </div>
  )
}


export default Home;