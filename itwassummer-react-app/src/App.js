// src/App.js

import React, {useEffect, useState} from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import SignUp from "./components/SignUp";
import Login from "./components/Login";
import Board from "./components/Board";


function App() {

  const [authenticated, setAuthenticated] = useState(false);
  useEffect(() => {
    const isAuthenticated = false; // Implement your authentication check logic
    setAuthenticated(isAuthenticated);
  }, []);

  return (
    // <div>
    //   {authenticated ? <p>User is authenticated</p> : <p>User is not authenticated</p>}
    // </div>
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<AuthGuard authenticated={authenticated}/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/signup" element={<SignUp/>}/>
        <Route path="/board" element={<Board/>}/>
      </Routes>
    </BrowserRouter>
  );
}

const AuthGuard = ({authenticated}) => {
  return authenticated ? <Navigate to="/board"/> : <Navigate to="/login"/>;
};

function getToken() {
  let auth = document.cookie
    .split('; ')
    .find(row => row.startsWith('Authorization'))
    ?.split('=')[1];

  if (auth === undefined) {
    return '';
  }

  // kakao 로그인 사용한 경우 Bearer 추가
  if (auth.indexOf('Bearer') === -1 && auth !== '') {
    auth = 'Bearer ' + auth;
  }
  return auth;
}

function isTokenExist() {

  if (getToken() === '') {
    return false
  } else {
    return true
  }
}


export default App;