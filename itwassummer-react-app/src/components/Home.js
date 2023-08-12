import React from "react";
import {useNavigate} from "react-router-dom";
import {useRecoilValue} from "recoil";
import {TokenAtom} from "../recoil/TokenAtom";

const Home = () => {

  const accessToken = useRecoilValue(TokenAtom)

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

      <p> 현재 recoilToken값 : {accessToken}</p>
    </div>
  )
}


export default Home;