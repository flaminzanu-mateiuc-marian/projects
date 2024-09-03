import { Link } from 'react-router-dom';

const AboutAppPage = () => {
    return (

        <div>
            <nav>
                <Link to='/'>Acasă</Link>
                <Link to='/about'>Despre</Link>
                <Link to='/myaccount'>Contul meu</Link>
            </nav>

        <h2>Bun venit pe platforma Freightbroker!<br></br>Aceasta vă permite să găsiți oferte în mod automat pentru mărfurile sau camioanele dumneavoastră!</h2><br></br>
        <ul>
            <li><h3>Pentru a vă crea un cont, accesați opțiunea „Creați un cont nou” din meniul principal și furnizați câmpurile solicitate.</h3></li>
            <li><h3>Pentru a vă autentifica, accesați opțiunea corespunzătoare contului dvs., apoi furnizați adresa de e-mail și parola.</h3></li>
            <li><h3>Pentru a adăuga un transport sau un camion nou, apăsați butonul „Adaugă un transport” sau „Adaugă un camion” și furnizați informațiile solicitate.</h3></li>
            <li><h3>Pentru a vedea mărfurile sau camioanele dvs., apăsați butonul „Transporturile dvs.” sau „Camioanele dvs.”</h3></li>
            <li><h3>Pentru a vedea o ofertă (dacă există), apăsați butonul „Vezi oferta”.”</h3></li>
            <li><h3>Pentru a accepta sau respinge o oferă, apăsați butonul „Acceptă oferta”, respectiv „Respinge oferta”.</h3></li>
            <li><h3>Dacă sunteți comerciant și oferta dvs. a fost acceptată bilateral, puteți efectua plata către transportator.</h3></li>
            <li><h3>Pentru a modifica datele aferente contului dvs., accesați opțiunea „Contul meu”.</h3></li>


        </ul>
        </div>
    )
}

export default AboutAppPage