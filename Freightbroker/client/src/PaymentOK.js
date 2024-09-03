import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const PaymentOK = () => {
    const navigate = useNavigate();

    useEffect(() => {
        alert("Plată efectuată cu succes!");
        navigate('/');
    }, [navigate]);
}

export default PaymentOK;
