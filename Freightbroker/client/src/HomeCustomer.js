import React, { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

const HomeCustomer = () => {
    const ipAddress = "192.168.51.208";
    const navigate = useNavigate();
    const [showOffer, setShowOffer] = useState(false);
    const [cargoMap, setCargoMap] = useState(new Map());
    const [myCargo, setMyCargo] = useState([]);
    const [showViewCargo, setShowViewCargo] = useState(false);
    const [showAddCargo, setShowAddCargo] = useState(false);
    const [dataLivrare, setDataLivrare] = useState('');
    const [greutate, setGreutate] = useState('');
    const [volum, setVolum] = useState('');
    const [cerinteSuplimentare, setCerinteSuplimentare] = useState('');
    const [adresaLivrare, setAdresaLivrare] = useState('');
    const [adresaRidicare, setAdresaRidicare] = useState('');
    const [comentarii, setComentarii] = useState('');
    const cookie = Cookies.get('token');

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

    const handleAddCargo = () => {
        setShowAddCargo(true);
        setShowViewCargo(false);
    }

    const getMyCargo = async () => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getmycargo', {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const cargoData = await response.json();
                setMyCargo(cargoData);
            } else {
                console.error('Failed to fetch cargo data');
            }
        } catch (error) {
            console.error('Error while fetching cargo data:', error);
        }
    };

    const handleMyCargo = async () => {
        getMyCargo();
        setShowViewCargo(true);
        setShowAddCargo(false);
    }

    const getOfferDetails = async (id_cargo) => {
        if (showOffer === false)
            setShowOffer(true);
        else
            setShowOffer(false);
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/getofferforcustomer?id_cargo=' + id_cargo, {
                method: 'GET',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                const cargoData = await response.json();
                const newCargoMap = new Map(cargoMap);
                newCargoMap.set(id_cargo, cargoData);
                setCargoMap(newCargoMap);
            } else {
                console.error('Failed to fetch cargo data');
            }
        } catch (error) {
            console.error('Error while fetching cargo data:', error);
        }
    }

    const acceptOfferClient = async (id_cargo) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/acceptofferforcustomer?id_cargo=' + id_cargo, {
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
    const rejectOfferClient = async (id_cargo) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/rejectofferforcustomer?id_cargo=' + id_cargo, {
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

    const doPayment = async (cargo, id_cargo) => {
        try {
            const response = await fetch('http://' + ipAddress + ':8085/api/freightbroker/pay?cost=' + cargo.cost + '&email=' + cargo.provider.email + '&id_cargo=' + id_cargo, {
                method: 'POST'
            });
            if (response.ok) {
                const responseData = await response.text();
                window.location.href = responseData;
            } else {
                alert('Eroare!');
            }
        } catch (error) {
            alert('Eroare la încărcarea cererii!');
            console.log(error);
        }
    }

    const formatDate = (dateString) => {
        const dateSplit = dateString.split('T')[0].split('-');
        const day = dateSplit[2];
        const month = dateSplit[1];
        const year = dateSplit[0];
        return `${day}.${month}.${year}`;
    }

    const addNewCargo = async (e) => {
        e.preventDefault();
        try {
            const token = Cookies.get('token');
            const requestBody = {
                data_critica_livrare: dataLivrare,
                greutate: greutate,
                volum: volum,
                cerinte_suplimentare: cerinteSuplimentare,
                adresa_ridicare: adresaRidicare,
                adresa_livrare: adresaLivrare,
                comentarii: comentarii,
                status: "Neonorată",
                latitudine_pct_ridicare: 0.0,
                longitudine_pct_ridicare: 0.0,
                latitudine_pct_livrate: 0.0,
                longitudine_pct_livrare: 0.0,
                distanta_cursa: 0.0
            };
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/addnewcargo', {
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
                alert('Eroare la încărcarea cererii! Este posibil ca adresa de încărcare sau de livrare să nu fi fost găsită! ');
            }
        } catch (error) {
            alert('Eroare la încărcarea cererii! Este posibil ca adresa de încărcare sau de livrare să nu fi fost găsită! ');
        }
    };

    const deleteCargo = async (id_cargo) => {
        try {
            const token = Cookies.get('token');
            const response = await fetch('http://' + ipAddress + ':8081/api/freightbroker/idm/deletecargo?id_cargo=' + id_cargo, {
                method: 'POST',
                headers: {
                    'Authorization': token
                }
            });
            if (response.ok) {
                alert("Marfa a fost ștearsă cu succes!");
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

            {showViewCargo && (
                <div>
                    {myCargo.map(cargoItem => (
                        <div key={cargoItem.id_cargo} className="cargoShow" style={{ backgroundColor: cargoItem.status === 'Neonorată' ? '#FFFF00' : '#90EE90' }}>
                            <p>Data livrătrii: {formatDate(cargoItem.data_critica_livrare)}</p>
                            <p>Greutate: {cargoItem.greutate} Kg</p>
                            <p>Volum: {cargoItem.volum} m³</p>
                            <p>Cerințe suplimentare: {cargoItem.cerinte_suplimentare}</p>
                            <p>Adresă ridicare: {cargoItem.adresa_ridicare}</p>
                            <p>Adresă livrare: {cargoItem.adresa_livrare}</p>
                            <p>Distanță cursă: {cargoItem.distanta_cursa.toFixed(2)} KM</p>
                            <p>Comentarii: {cargoItem.comentarii}</p>
                            <p>Status: {cargoItem.status}</p>
                            {cargoItem.status === 'Rezervat' && (
                                <button onClick={() => getOfferDetails(cargoItem.id_cargo)}>{showOffer ? 'Ascunde oferta' : 'Vezi oferta'}</button>
                            )}
                            <div><button onClick={() => deleteCargo(cargoItem.id_cargo)}>Ștergeți cursa</button></div>

                            {showOffer === true &&

                                cargoMap.get(cargoItem.id_cargo) && (

                                    <div style={{ color: '#000000' }}>
                                        <p style={{ fontWeight: 'bold' }}>Informații despre transportator:</p>
                                        <p>Nume: {cargoMap.get(cargoItem.id_cargo).provider.nume} {cargoMap.get(cargoItem.id_cargo).provider.prenume}</p>
                                        <p>Adresă: {cargoMap.get(cargoItem.id_cargo).provider.adresa}</p>
                                        <p>Email: {cargoMap.get(cargoItem.id_cargo).provider.email}</p>
                                        <p>Telefon: {cargoMap.get(cargoItem.id_cargo).provider.telefon}</p>
                                        <p style={{ fontWeight: 'bold' }}>Cost transport: {cargoMap.get(cargoItem.id_cargo).cost.toFixed(2)} EUR</p>

                                        {cargoMap.get(cargoItem.id_cargo).acceptedByClient !== 0 && (
                                            <div style={{ color: "black" }}>
                                                <p>Ați acceptat deja oferta.</p>
                                            </div>
                                        )}

                                        {cargoMap.get(cargoItem.id_cargo).acceptedByClient === 0 && (
                                            <div>
                                                <button onClick={() => acceptOfferClient(cargoItem.id_cargo)}>Acceptă oferta</button>
                                                <button onClick={() => rejectOfferClient(cargoItem.id_cargo)}>Respinge oferta</button>

                                            </div>
                                        )}
                                        {cargoMap.get(cargoItem.id_cargo).acceptedByClient === 1 && (
                                            <div>
                                                <button onClick={() => rejectOfferClient(cargoItem.id_cargo)}>Respinge oferta</button>
                                            </div>
                                        )}

                                        {cargoMap.get(cargoItem.id_cargo).acceptedByProvider === 1 && (
                                            <div style={{ color: "black" }}>
                                                <p>Transportatorul a acceptat deja oferta.</p>
                                            </div>
                                        )}
                                        {cargoMap.get(cargoItem.id_cargo).acceptedByProvider === 1 && cargoMap.get(cargoItem.id_cargo).acceptedByClient === 1 && (
                                            <div><button onClick={() => doPayment(cargoMap.get(cargoItem.id_cargo), cargoItem.id_cargo)}>Efectuați plata</button></div>
                                        )}


                                    </div>
                                )}
                        </div>
                    ))}
                </div>
            )}
            {showAddCargo && (
                <div>
                    <form onSubmit={addNewCargo} className="AddForm">
                        <div>
                            <label>Dată livrare:</label>
                            <input
                                type="date"
                                value={dataLivrare}
                                onChange={(e) => setDataLivrare(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Greutate (kg):</label>
                            <input
                                type="text"
                                value={greutate}
                                onChange={(e) => setGreutate(e.target.value)}
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
                            <label>Cerințe suplimentare:</label><br></br>
                            <textarea
                                value={cerinteSuplimentare}
                                placeholder='Completați rubrica sau introduceți un spațiu'
                                style={{ height: '200px', width: '90%', marginLeft: '4%' }}
                                onChange={(e) => setCerinteSuplimentare(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Adresă ridicare:</label>
                            <input
                                type="text"
                                value={adresaRidicare}
                                placeholder='Strada, Numar, Localitate, Județ/Stat, Țară'
                                onChange={(e) => setAdresaRidicare(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Adresă livrare:</label>
                            <input
                                type="text"
                                value={adresaLivrare}
                                placeholder='Strada, Numar, Localitate, Județ/Stat, Țară'
                                onChange={(e) => setAdresaLivrare(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label>Comentarii:</label>
                            <input
                                type="text"
                                placeholder='Completați rubrica sau introduceți un spațiu'
                                value={comentarii}
                                onChange={(e) => setComentarii(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit">Submit</button>

                    </form>
                </div>
            )}
            <button onClick={handleAddCargo}>Adăugați un transport</button>
            <button onClick={handleMyCargo}>Transporturile dvs.</button>
            <button onClick={handleLogOut}>Log out</button>
        </div>
    )
}

export default HomeCustomer;
