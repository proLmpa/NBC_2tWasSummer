// src/App.js

import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import SignUp from "./components/SignUp";
import Login from "./components/Login";
import Board from "./components/Board";
import ProtectedRoute from "./Routes/ProtectedRoute";
import Home from "./components/Home";


function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Home/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/signup" element={<SignUp/>}/>

        {/*유저전용 페이지들*/}
        <Route element={<ProtectedRoute/>}>
          <Route path="/board" element={<Board/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}


export default App;