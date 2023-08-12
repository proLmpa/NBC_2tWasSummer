import React from 'react';
import {useNavigate} from "react-router-dom";

function Board() {

  const navigate = useNavigate()
  const handleHomeClick = () => {
    navigate('/') // v6
  };


  return (
    <div>
      보드입니다dddd

      <button onClick={handleHomeClick}>Go Home</button>

    </div>
  );
}

export default Board;