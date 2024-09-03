import { useNavigate } from 'react-router-dom';

const ErrorPage = () => {
    const navigate = useNavigate();
    const handleRedirect = () => {
        navigate('/');
    }
    return (
        <div className="errorPage">
            A intervenit o eroare!
            <button onClick={handleRedirect}>Revenire la prima pagină</button>
        </div>
    );
};

export default ErrorPage;
