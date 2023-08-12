import React from 'react';
import {useNavigate} from "react-router-dom";
import {useRecoilValue, useSetRecoilState} from "recoil";
import {isLoginSelector, TokenAtom} from "../recoil/TokenAtom";
import Card from "./Card";

function Board() {

  const navigate = useNavigate()

  const isLogin = useRecoilValue(isLoginSelector)
  const setAccessToken = useSetRecoilState(TokenAtom)


  // useEffect(() => {
  //   if (!isLogin) {
  //     navigate('/login')
  //   }
  // }, [])

  const handleHomeClick = () => {
    navigate('/') // v6
  }

  const handleLogoutClick = () => {
    setAccessToken(undefined)
    navigate('/')
  }


  return (
    <div>
      보드입니다
      <button onClick={handleHomeClick}>Go Home</button>
      <button onClick={handleLogoutClick}>LogOut</button>

      <div>login상태 : {isLogin ? 'y' : 'n'}</div>
      <br/><br/>

      <br/>
      <Card title='타이틀 12' user='wooin'/>

    </div>);


}

export default Board;