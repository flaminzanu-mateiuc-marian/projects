package com.hospital.appointments_service.services;


import com.hospital.appointments_service.dto.AppointmentDTO;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    public AppointmentDTO save(AppointmentDTO appointment);

    public List<AppointmentDTO> getAll();

    public List<AppointmentDTO> getAppointmentsForUser(long id);
}