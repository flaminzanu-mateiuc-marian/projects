import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const PaymentError = () => {
    const navigate = useNavigate();

    useEffect(() => {
        alert("Eroare la procesarea plății! Este posibil să fi efectuat deja plata!");
        navigate('/');
    }, [navigate]);
}

export default PaymentError;
