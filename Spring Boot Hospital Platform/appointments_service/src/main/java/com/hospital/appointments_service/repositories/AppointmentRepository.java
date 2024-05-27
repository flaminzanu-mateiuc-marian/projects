package com.hospital.appointments_service.repositories;

import com.hospital.appointments_service.dto.AppointmentDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AppointmentRepository extends CrudRepository<AppointmentDTO,Long> {

}
