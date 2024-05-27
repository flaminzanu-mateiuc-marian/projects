package com.hospital.appointments_service.services;

import com.hospital.appointments_service.dto.AppointmentDTO;
import com.hospital.appointments_service.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointment) {
        appointmentRepository.save(appointment);
        return appointment;
    }

    @Override
    public List<AppointmentDTO> getAll() {
        List<AppointmentDTO> list = (List<AppointmentDTO>) appointmentRepository.findAll();
        return list;
    }
    @Override
    public List<AppointmentDTO> getAppointmentsForUser(long id){
        List<AppointmentDTO> list = new ArrayList<AppointmentDTO>();
        for (AppointmentDTO appointment : getAll())
        {
            if (appointment.getIdPacient() == id)
                list.add(appointment);
        }
        System.out.println("Am gasit " + list.size() +" intrari");
        return list;
    }

}
