import React, { useState } from 'react';
import { BrowserRouter as Router, Link, Route, Routes } from 'react-router-dom';
import LoginForm from './LoginForm';
import './App.css';
import HomeForm from './HomeForm';

const Welcome = () => (
  <div>
  </div>
);

const App = () => {
  const [isLoggedIn, setLoggedIn] = useState(false);

  const handleLogin = () => {
    // Add your login logic here and update the state accordingly
    setLoggedIn(true);
  };

  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <Routes>
            <Route path="/" element={<Welcome />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/home" element={<HomeForm/>}/>
          </Routes>
          {!isLoggedIn && (
            <Link to="/login">
              <button className="App-link" onClick={handleLogin}>
                Login
              </button>
            </Link>
          )}
        </header>
      </div>
    </Router>
  );
};

export default App;
