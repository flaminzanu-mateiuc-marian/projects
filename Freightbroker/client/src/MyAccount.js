import { Link, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import React, { useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';

const MyAccount = () => {
    const [userType, setUserType] = useState("");
    const cookie = Cookies.get('token');
    const navigate = useNavigate();
    const ipAddress = "192.168.51.208";
    const [nume, setNume] = useState('');
    const [prenume, setPrenume] = useState('');
    const [adresaTransportator, setAdresaTransportator] = useState('');
    const [email, setEmail] = useState('');
    const [parola, setParola] = useState('');
    const [confirmParola, setConfirmParola] = useState('');
    const [telefon, setTelefon] = useState('');
    const bcrypt = require('bcryptjs');
    const [id, setId] = useState();

    useEffect(() => {
        if (!cookie) {
            navigate('/');
        } else {
            try {
                const decodedToken = jwtDecode(cookie);
                setId(decodedToken.id_customer);
                if (decodedToken.user_type === 'comerciant') {
                    setUserType("customer");
                } else if (decodedToken.user_type === 'transportator') {
                    setUserType("provider");
                }
            } catch (error) {
                console.error("Error decoding token:", error);
                navigate('/');
            }
        }
    }, [cookie, navigate]);

    useEffect(() => {
        if (userType === "customer") {

            getCustomer();
        } else if (userType === "provider") {
            getProvider();
        }
    }, [userType]);


    const getCustomer = async () => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getCustomer', {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const customerData = await response.json();
                setNume(customerData.nume);
                setPrenume(customerData.prenume);
                setEmail(customerData.email);
                setTelefon(customerData.telefon);
            } else {
                console.error('Failed to fetch customer data');
            }
        } catch (error) {
            console.error('Error while fetching customer data:', error);
        }
    };

    const getProvider = async () => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getProvider', {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const customerData = await response.json();
                setNume(customerData.nume);
                setPrenume(customerData.prenume);
                setAdresaTransportator(customerData.adresa);
                setEmail(customerData.email);
                setTelefon(customerData.telefon);
            } else {
                console.error('Failed to fetch customer data');
            }
        } catch (error) {
            console.error('Error while fetching customer data:', error);
        }
    };


    const cancelUpdate = () => {
        navigate('/');
    };


    const handleSubmit = async (e) => {
        e.preventDefault();

        if (parola !== confirmParola) {
            alert('Câmpurile "Parolă" și "Confirmare parolă" nu coincid. Încercați din nou!');
            setParola('');
            setConfirmParola('');
            return;
        } else
            if (!/^[0-9]*$/.test(telefon)) {
                alert('Numărul de telefon nu este valid. Încercați din nou!');
                setTelefon('');
                return;
            }
            else
                if (!email.toLowerCase().match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)) {
                    alert('Adresa de e-mail este invalidă. Încercați din nou!');
                    setEmail('');
                    return;
                } else {

                    let hashedPwd = '';
                    if (parola !== '') {
                        hashedPwd = await bcrypt.hash(parola, 10);
                    }

                    const user = {
                        id_customer: id,
                        nume,
                        prenume,
                        email,
                        telefon,
                        parola: hashedPwd
                    };

                    if (userType === "provider") {
                        user.adresa = adresaTransportator;
                    }

                    var url = "";
                    if (userType === "customer")
                        url = 'http://' + ipAddress + ':8080/api/freightbroker/updateCustomer';
                    else url = 'http://' + ipAddress + ':8080/api/freightbroker/updateProvider';
                    try {
                        const response = await fetch(url, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify(user),
                        });

                        if (response.ok) {
                            alert('Utilizator actualizat cu succes!');
                            navigate('/');
                        } else {
                            alert('Utilizatorul nu a fost actualizat! Incercati din nou');
                        }
                    } catch (error) {
                        console.error('Error updating user:', error);
                    }
                }
    };


    return (
        <div>
            <nav>
                <Link to='/'>Acasă</Link>
                <Link to='/about'>Despre</Link>
                <Link to='/myaccount'>Contul meu</Link>
            </nav>
            <form onSubmit={handleSubmit}>

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
                        readOnly={true}
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={{ backgroundColor: '#6c757d', color: '#e9ecef', cursor: 'not-allowed' }}
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
                {userType === "provider" &&
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
                <button type="submit" className="submitFormButton">Actualizare</button>
                <button onClick={cancelUpdate}>Anulare</button>
            </form>

        </div>


    );
}

export default MyAccount;
