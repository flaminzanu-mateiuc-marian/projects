import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './App.css';

const HomeForm = () => {
  const { state } = useLocation();
  const navigate = useNavigate();

  const [appointments, setAppointments] = useState([]);
  const [showAppointments, setShowAppointments] = useState(false);
  const [showNewAppointmentForm, setShowNewAppointmentForm] = useState(false);
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  useEffect(() => {
    if (state && state.token) {
      const token = state.token;

      // Fetch doctors
      axios.get('http://localhost:8080/api/medical_office/physicians', {

      })
      .then(response => {
        setDoctors(response.data._embedded.physicianDTOList);
      })
      .catch(error => {
        console.error('Error fetching doctors:', error);
      });

      axios.get('http://localhost:8082/api/medical_office/appointments/PastAppointments', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      .then(response => {
        setAppointments(response.data?._embedded?.appointmentDTOList || []);

      })
      .catch(error => {
        console.error('Error fetching appointments:', error);
      });
    }
  }, [state]);

  const handleShowAppointments = () => {
    setShowAppointments(true);
    setShowNewAppointmentForm(false);
  };

  const handleShowNewAppointmentForm = () => {
    setShowAppointments(false);
    setShowNewAppointmentForm(true);
  };

  const handleDoctorChange = (event) => {
    setSelectedDoctor(event.target.value);
  };

  const handleDateChange = (event) => {
    setSelectedDate(event.target.value);
  };

  const handleNewAppointmentSubmit = async (event) => {
    event.preventDefault();
  
    try {
      const appointmentData = {
        id_pacient: 1,
        id_doctor: selectedDoctor,
        data: selectedDate,
        status: 'Neprezentata',
      };
  
      const response = await axios.post(
        'http://localhost:8082/api/medical_office/appointments',
        appointmentData,
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );
  
      alert('Programare adaugata cu succes!:');
  
      setSelectedDoctor('');
      setSelectedDate('');
      setShowNewAppointmentForm(false);
  
    } catch (error) {
      console.error('Eroare la adaugare programare');
    }
  };
  
  return (
    <div>
      <h1>Buna ziua!</h1>
      <button onClick={handleShowNewAppointmentForm}>Programare noua</button>
      <button onClick={handleShowAppointments}>Programari anterioare</button>
      <button>Rezultate analize</button>

      {showNewAppointmentForm ? (
        <form onSubmit={handleNewAppointmentSubmit}>
          <label>Alegeti un doctor:</label>
          <select onChange={handleDoctorChange} value={selectedDoctor} required>
            <option value="">-//-</option>
            {doctors.map(doctor => (
              <option key={doctor.id_doctor} value={doctor.id_doctor}>
                {`${doctor.nume} ${doctor.prenume}`}
              </option>
            ))}
          </select><br></br>
          <label>Selectati data:</label>
          <input
            type="date"
            value={selectedDate}
            onChange={handleDateChange}
            required
          />
          <button type="submit">Submit</button>
        </form>
      ) : 
      //Programari anterioare
      (
        <div>
          <h2>Programari anterioare</h2>
          <ul>
            {appointments.map(appointment => (
              <li key={appointment.idPacient}>
                Data: {appointment.data}, Status: {appointment.status}
              </li>
            ))}
          </ul>
        </div>
      )}
      
    </div>
  );
};

export default HomeForm;
