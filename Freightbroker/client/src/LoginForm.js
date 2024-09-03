import React, { useState, useEffect } from 'react';
import { Root } from 'protobufjs';
import proto from "./auth.proto";
import { useNavigate, useLocation } from 'react-router-dom';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import { Link } from 'react-router-dom';


const LoginForm = () => {
  const ipAddress = '192.168.51.208';
  const location = useLocation();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const cookie = Cookies.get('token');
  const userType = (location.state !== null && location.state.userType !== "null" && location.state.userType !== undefined && location.state.userType !== "undefined") ? location.state.userType : undefined;
  const [showUserLoginCustomer, setShowUserLoginCustomer] = useState(false);
  const [showUserLoginProvider, setShowUserLoginProvider] = useState(false);

  useEffect(() => {
    if (cookie) {
      const decodedToken = jwtDecode(cookie);
      if (decodedToken.user_type === 'comerciant') {
        navigate('/homecustomer', { state: { userType: "customer" } });
      } else if (decodedToken.user_type === 'transportator') {
        navigate('/hometransporter', { state: { userType: "provider" } });
      }
    } else if (location.state == null) {
      navigate('/');
    } else {
      if (userType === "Comerciant") {
        setShowUserLoginCustomer(true);
        setShowUserLoginProvider(false);
      } else {
        setShowUserLoginCustomer(false);
        setShowUserLoginProvider(true);
      }
    }
  }, [cookie, location.state, navigate, userType]);
  const handleLogin = async () => {
    try {
      const root = await new Root().load(proto);
      const AuthRequest = root.lookupType('AuthRequest');
      const message = AuthRequest.create({
        email: email,
        password: password,
        userType: userType
      });

      const buffer = AuthRequest.encode(message).finish();
      const response = await fetch('http://' + ipAddress + ':8080/api/freightbroker/authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-protobuf',
        },
        body: buffer,
        credentials: 'include'
      });
      if (response.ok) {
        var user_cookie = await response.text();
        Cookies.set('token', user_cookie, { expires: new Date(Date.now() + 3 * 3600 * 1000), sameSite:'Strict'});
        if (userType === "Comerciant")
          navigate('/homecustomer')
        else if (userType === "Transportator")
          navigate('/hometransporter')
      } else {
        Cookies.remove('token');
        alert('Utilizatorul nu a fost găsit!');
        setEmail('');
        setPassword('');
      }
    } catch (error) {
      Cookies.remove('token');
      alert(error);
    }
  };


  return (
    <div>
      <nav>
        <Link to='/'>Acasă</Link>
        <Link to='/about'>Despre</Link>
        <Link to='/myaccount'>Contul meu</Link>
      </nav>
      {showUserLoginCustomer && (
        <div>
          Meniu autentificare comercianți<br></br>
        </div>
      )}
      {showUserLoginProvider && (
        <div>
          Meniu autentificare transportatori<br></br>
        </div>
      )}
      <h2>Email:</h2> <input
        id="emailInput"
        type="text"
        placeholder="E-mail"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <h2>Parola:</h2><input
        id="passwordInput"
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginForm;
