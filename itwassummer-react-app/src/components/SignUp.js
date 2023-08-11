// src/components/SignUp.js

import React from 'react';

function SignUp() {
  fetch('http://localhost:8080/api/users/signup/test')
    .then((response) => response.json())
    .then((data) => console.log(data))

  const name = 'dd'

  return (
    <div>
      <h1>회원가입</h1>

      {name}

    </div>
  )
}

export default SignUp;