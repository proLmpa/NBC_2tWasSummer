// src/App.js

import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import SignUp from "./components/SignUp";
import Login from "./components/Login";
import Home from "./components/Home";
import Board from "./components/Board";


function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Home/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/signup" element={<SignUp/>}/>
        <Route path="/board" element={<Board/>}/>

        {/*유저전용 페이지들*/}
        {/*<Route element={<ProtectedRoute/>}>*/}
        {/*</Route>*/}
      </Routes>
    </BrowserRouter>
  );
}


export default App;