import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import LoginForm from './LoginForm';
import HomeCustomer from './HomeCustomer';
import './App.css';
import Cookies from 'js-cookie';
import ErrorPage from './ErrorPage';
import AboutAppPage from './AboutAppPage';
import { jwtDecode } from 'jwt-decode';
import HomeTransporter from './HomeTransporter';
import { Link } from 'react-router-dom';
import MyAccount from './MyAccount';
import PaymentOK from './PaymentOK';
import PaymentError from './PaymentError';
const bcrypt = require('bcryptjs');

const EntryPoint = () => {
  const ipAddress = "192.168.51.208";
  const [showNewAccount, setShowNewAccount] = useState(false);
  const cookie = Cookies.get('token');
  const navigate = useNavigate();
  const [userType, setUserType] = useState('Comerciant');
  const [nume, setNume] = useState('');
  const [prenume, setPrenume] = useState('');
  const [adresaTransportator, setAdresaTransportator] = useState('');
  const [email, setEmail] = useState('');
  const [parola, setParola] = useState('');
  const [confirmParola, setConfirmParola] = useState('');
  const [telefon, setTelefon] = useState('');


  useEffect(() => {
    if (cookie) {
      const decodedToken = jwtDecode(cookie);
      if (decodedToken.user_type === 'comerciant') {
        navigate('/homecustomer');
      } else if (decodedToken.user_type === 'transportator') {
        navigate('/hometransporter');
      }
    }
    else {
      if(Cookies.get('token'))
        Cookies.remove('token');
      navigate('/');
    }

  }, [cookie, navigate]);

  const handleCheckboxChange = (e) => {
    setUserType(e.target.value);
  };

  const handleSubmit = async (e) => {

    var response = undefined;
    e.preventDefault();

    if (userType === "Comerciant") {
      if (parola === confirmParola && parola !== '' && confirmParola !== '') {

        if (!email.toLowerCase().match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)) {
          alert('Adresa de e-mail este invalidă. Încercați din nou!');
          setEmail('');
        } else if (!/^[0-9]*$/.test(telefon)) {
          alert('Numărul de telefon nu este valid. Încercați din nou!');
          setTelefon('');
          return;
        } else {
          const hashedPwd = await bcrypt.hash(parola, 10);
          const user = {
            nume,
            prenume,
            email,
            telefon,
            parola: hashedPwd
          };
          try {
            response = await fetch('http://' + ipAddress + ':8080/api/freightbroker/createCustomer', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(user),
            });

            if (response.ok) {
              await response.json();
              alert('Utilizator adaugat cu succes!');
              navigate('/');
            } else {
              alert('Utilizatorul nu a fost creat! Incercati din nou');
            }
          } catch (error) {
            console.error(error);
          }

        }

      } else {
        alert('Câmpurile "Parolă" și "Confirmare parolă" nu coincid. Încercați din nou!');
        setParola('');
        setConfirmParola('');
      }
    } else {
      if (parola === confirmParola) {
        if (!email.toLowerCase().match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)) {
          alert('Adresa de e-mail este invalidă. Încercați din nou!');
          setEmail('');
        } else if (!/^[0-9]*$/.test(telefon)) {
          alert('Numărul de telefon nu este valid. Încercați din nou!');
          setTelefon('');
          return;
        } else {
          const hashedPwd = await bcrypt.hash(parola, 10);
          const user = {
            nume,
            prenume,
            email,
            telefon,
            adresa: adresaTransportator,
            parola: hashedPwd
          };

          try {
            response = await fetch('http://' + ipAddress + ':8080/api/freightbroker/createProvider', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(user),
            });

            if (response.ok) {
              await response.json();
              alert('Utilizator adaugat cu succes!');
              navigate('/');

            } else {
              alert('Utilizatorul nu a fost creat! Incercati din nou');
            }
          } catch (error) {
            console.error(error);
          }
        }
      } else {
        alert('Campurile "Parola" si "Confirmare parola" nu coincid. Incercati din nou!');
        setParola('');
        setConfirmParola('');
      }
    }

  };



  const setCustomer = () => {
    const userType = "Comerciant";
    navigate('/login', { state: { userType: userType } });
  };
  const handleNewAccount = () => {
    setShowNewAccount(true);

  }

  const cancelNewAccount = () => {
    setShowNewAccount(false);
  }

  const setProvider = () => {
    const userType = "Transportator";
    navigate('/login', { state: { userType: userType } });
  };
  return (
    <div>
      <nav>
        <Link to='/'>Acasă</Link>
        <Link to='/about'>Despre</Link>
        <Link to='/myaccount'>Contul meu</Link>
      </nav>

      <div className="appIntro">
        <img src="/freightbroker_logo.png" alt="Logo" width="200" height="200" />
        <h1>Freightbroker</h1>
      </div>
      <h2>Sunteți...</h2>
      <button onClick={setCustomer}>Comerciant</button>
      <button onClick={setProvider}>Transportator</button><br></br>
      <button onClick={handleNewAccount}>Client nou? Creați un cont nou!</button>

      {showNewAccount && (
        <form onSubmit={handleSubmit}>
          <div>
            <input
              type="checkbox"
              value="Comerciant"
              onChange={handleCheckboxChange}
              checked={userType === "Comerciant"}
            />
            <label>Comerciant</label>
            <input
              type="checkbox"
              value="Transportator"
              onChange={handleCheckboxChange}
              checked={userType === "Transportator"}
            />
            <label>Transportator</label>
          </div>
          <div>
            <label>Nume:</label>
            <input
              type="text"
              value={nume}
              onChange={(e) => setNume(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Prenume:</label>
            <input
              type="text"
              value={prenume}
              onChange={(e) => setPrenume(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Email:</label>
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Parola:</label>
            <input
              type="password"
              value={parola}
              onChange={(e) => setParola(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Confirmare parola:</label>
            <input
              type="password"
              value={confirmParola}
              onChange={(e) => setConfirmParola(e.target.value)}
              required
            />
          </div>
          {userType === "Transportator" &&
            <div>
              <label>Adresă:</label>
              <input
                type="text"
                value={adresaTransportator}
                onChange={(e) => setAdresaTransportator(e.target.value)}
                required
              />
            </div>
          }
          <div>
            <label>Telefon:</label>
            <input
              type="text"
              value={telefon}
              onChange={(e) => setTelefon(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="submitFormButton">Submit</button>
          <button onClick={cancelNewAccount}>Anulare</button>
        </form>
      )}
    </div>

  );
};

const App = () => {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <Routes>
            <Route path="/" element={<EntryPoint />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/homecustomer" element={<HomeCustomer />} />
            <Route path="/hometransporter" element={<HomeTransporter />} />
            <Route path="/errorpage" element={<ErrorPage />} />
            <Route path="/about" element={<AboutAppPage />} />
            <Route path="/myaccount" element={<MyAccount />} />
            <Route path="/paymentok" element={<PaymentOK />} />
            <Route path="/paymenterror" element={<PaymentError />} />

          </Routes>
        </header>
      </div>
    </Router>
  );
};

export default App;
