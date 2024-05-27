import React, { useState } from 'react';
import { Root } from 'protobufjs';
import proto from "./auth.proto";
import * as jwt_decode from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

const LoginForm = ({ onLogin, isLoggedIn }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      setLoading(true);

      const root = await new Promise((resolve, reject) => {
        const loadedRoot = new Root();
        loadedRoot.load(proto, (err) => {
          if (err) {
            reject(err);
          } else {
            resolve(loadedRoot);
          }
        });
      });

      const AuthRequest = root.lookupType('AuthRequest');
      const message = AuthRequest.create({
        username: username,
        password: password,
      });

      const buffer = AuthRequest.encode(message).finish();
      console.log("Buffer = " + buffer);
      const response = await fetch('http://localhost:8085/api/medical_office/authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-protobuf',
        },
        body: buffer,
      });

      if (response.ok) {
        const data = await response.text();
        console.log('Success:', data);

        const decodedToken = jwt_decode.jwtDecode(data);
        const extractedUsername = decodedToken.username;
        console.log(extractedUsername);
        navigate('/home', { state: { token: data } });
        onLogin();
      } else {
        console.error('HTTP Error:', response.status, response.statusText);
      }
    } catch (error) {
      console.error('Network error:', error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin} disabled={loading}>
        {isLoggedIn ? 'Logout' : 'Login'}
      </button>
    </div>
  );
};

export default LoginForm;
