import React, { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

const HomeTransporter = () => {
    const ipAddress = '192.168.51.208';
    const [showOffer, setShowOffer] = useState(false);
    const navigate = useNavigate();
    const [showViewTruck, setShowViewTruck] = useState(false);
    const [showAddTruck, setShowAddTruck] = useState(false);
    const cookie = Cookies.get('token');
    const [truckMap, setTruckMap] = useState(new Map());
    const [tonaj, setTonaj] = useState('');
    const [volum, setVolum] = useState('');
    const [pret, setPret] = useState('');
    const [locatieInitiala, setLocatieInitiala] = useState('');
    const [costTransport, setCostTransport] = useState('');
    var [myTrucks, setMyTruck] = useState([]);

    useEffect(() => {
        if (!cookie)
            navigate('/errorpage');
    }, [cookie, navigate]);

    const handleLogOut = () => {
        const confirmLogout = window.confirm("Sunteți sigur că doriți să vă deautentificați?");
        if (confirmLogout) {
            Cookies.remove('token');
            navigate('/');
        }
    }
    const getOfferDetails = async (id_truck) => {
        if (showOffer === false)
            setShowOffer(true);
        else
            setShowOffer(false);
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getofferforprovider?id_truck=' + id_truck, {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const truckData = await response.json();
                //console.log(truckData);
                const newTruckMap = new Map(truckMap);
                newTruckMap.set(id_truck, truckData);
                setTruckMap(newTruckMap);
            } else {
                console.error('Failed to fetch cargo data');
            }
        } catch (error) {
            console.error('Error while fetching cargo data:', error);
        }
    }

    const acceptOfferProvider = async (id_truck) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/acceptofferforprovider?id_truck=' + id_truck, {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                alert("Ați acceptat cu succes oferta!");
                window.location.reload();

            } else {
                console.error('Eroare la procesare');
            }
        } catch (error) {
            console.error('Eroare la procesare');
        }
    }
    const rejectOfferProvider = async (id_truck) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/rejectofferforprovider?id_truck=' + id_truck, {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                alert("Cerere executată cu succes!");
                window.location.reload();
            } else {
                console.error('Eroare la procesare');
            }
        } catch (error) {
            console.error('Eroare la procesare');
        }
    }

    const getMyTruck = async () => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getmytruck', {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const truckData = await response.json();
                setMyTruck(truckData);
            } else {
                console.error('Failed to fetch cargo data');
            }
        } catch (error) {
            console.error('Error while fetching cargo data:', error);
        }
    };

    const handleAddTruck = () => {
        setShowAddTruck(true);
        setShowViewTruck(false);
    }

    const handleMyTruck = () => {
        getMyTruck();
        setShowViewTruck(true);
        setShowAddTruck(false);
    }

    const addNewTruck = async (e) => {
        e.preventDefault();
        try {
            const token = Cookies.get('token');
            const requestBody = {
                locatieInitiala: locatieInitiala,
                volum: volum,
                pretKM: pret,
                costTransport: costTransport,
                latitudineLocatieInitiala: 0.0,
                longitudineLocatieInitiala: 0.0,
                tonajMaxim: tonaj,
                status: "Liber"
            };

            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/addnewtruck', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
                body: JSON.stringify(requestBody),
            });
            if (response.ok) {
                alert('Cererea a fost încărcată cu succes!');
            } else {
                alert('Eroare la încărcarea cererii! Este posibil ca locația inițială să nu fi fost găsită! ');
            }
        } catch (error) {
            alert('Eroare la încărcarea cererii! Este posibil ca locația inițială să nu fi fost găsită! ');
        }
    }
    const deleteTruck = async (id_truck) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/deletetruck?id_truck=' + id_truck, {
                method: 'POST',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                alert("Camionul a fost șters cu succes!");
            } else {
                console.error('Eroare la procesare');
            }
        } catch (error) {
            console.error('Eroare la procesare');
        }
    }
    return (
        <div>
            <nav>
                <Link to='/'>Acasă</Link>
                <Link to='/about'>Despre</Link>
                <Link to='/myaccount'>Contul meu</Link>
            </nav>


            {showViewTruck && (
                <div>
                    {myTrucks.map(truck => (
                        <div key={truck.id_truck} className="truckShow" style={{ backgroundColor: truck.status === 'Liber' ? '#FFFF00' : '#b6fab9' }}>
                            <p>Locație inițială: {truck.locatieInitiala}</p>
                            <p>Tonaj maxim: {truck.tonajMaxim}</p>
                            <p>Volum: {truck.volum}</p>
                            <p>Preț/KM (EUR): {truck.pretKM}</p>
                            <p>Cost Transport/KM (EUR): {truck.costTransport}</p>
                            <p>Status: {truck.status}</p>

                            {truck.status === 'Rezervat' && (
                                <button onClick={() => getOfferDetails(truck.id_truck)}>{showOffer ? 'Ascunde oferta' : 'Vezi oferta'}</button>
                            )}
                            <div><button onClick={() => deleteTruck(truck.id_truck)}>Ștergeți camionul</button></div>

                            {showOffer === true &&
                                truckMap.get(truck.id_truck) && (
                                    <div style={{ color: '#000000' }}>
                                        <p style={{ fontWeight: 'bold' }}>Informații despre comerciant:</p>
                                        <p>Nume: {truckMap.get(truck.id_truck).customer.nume} {truckMap.get(truck.id_truck).customer.prenume}</p>
                                        <p>Email: {truckMap.get(truck.id_truck).customer.email}</p>
                                        <p>Telefon: {truckMap.get(truck.id_truck).customer.telefon}</p>
                                        <p style={{ fontWeight: 'bold' }}>Informații despre marfă:</p>
                                        <p>Adresă ridicare: {truckMap.get(truck.id_truck).cargo.adresa_ridicare}</p>
                                        <p>Adresă livrare: {truckMap.get(truck.id_truck).cargo.adresa_livrare}</p>
                                        <p>Cerințe suplimentare: {truckMap.get(truck.id_truck).cargo.cerinte_suplimentare}</p>
                                        <p>Comentarii: {truckMap.get(truck.id_truck).cargo.comentarii}</p>

                                        <p style={{ fontWeight: 'bold' }}>Profit: {truckMap.get(truck.id_truck).profit.toFixed(2)} EUR</p>

                                        {truckMap.get(truck.id_truck).acceptedByProvider !== 0 && (
                                            <div style={{ color: "black" }}>
                                                <p>Ați acceptat deja oferta.</p>
                                            </div>
                                        )}

                                        {truckMap.get(truck.id_truck).acceptedByProvider === 0 && (
                                            <div>
                                                <button onClick={() => acceptOfferProvider(truck.id_truck)}>Acceptă oferta</button>
                                                <button onClick={() => rejectOfferProvider(truck.id_truck)}>Respinge oferta</button>
                                            </div>
                                        )}
                                        {truckMap.get(truck.id_truck).acceptedByProvider === 1 && (
                                            <div>
                                                <button onClick={() => rejectOfferProvider(truck.id_truck)}>Respinge oferta</button>
                                            </div>
                                        )}
                                        {truckMap.get(truck.id_truck).acceptedByClient !== 0 && (
                                            <div style={{ color: "black" }}>
                                                <p>Comerciantul a acceptat deja oferta.</p>
                                            </div>
                                        )}

                                    </div>
                                )}
                        </div>
                    ))}
                </div>
            )}


            {showAddTruck && (
                <div>
                    <form onSubmit={addNewTruck} className="AddForm">

                        <div>
                            <label>Locație inițială:</label>
                            <input
                                type="text"
                                value={locatieInitiala}
                                onChange={(e) => setLocatieInitiala(e.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label>Tonaj maxim (kg):</label>
                            <input
                                type="text"
                                value={tonaj}
                                onChange={(e) => setTonaj(e.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label>Volum (m³):</label>
                            <input
                                type="text"
                                value={volum}
                                onChange={(e) => setVolum(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Preț/KM (EUR):</label>
                            <input
                                type="text"
                                value={pret}
                                onChange={(e) => setPret(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Cost transport/KM(EUR):</label>
                            <input
                                type="text"
                                value={costTransport}
                                onChange={(e) => setCostTransport(e.target.value)}
                                required
                            />
                        </div>

                        <button type="submit">Submit</button>

                    </form>
                </div>
            )}
            <button onClick={handleAddTruck}>Adăugați un camion</button>
            <button onClick={handleMyTruck}>Camioanele dvs.</button>
            <button onClick={handleLogOut}>Log out</button>
        </div>
    )
}

export default HomeTransporter;
