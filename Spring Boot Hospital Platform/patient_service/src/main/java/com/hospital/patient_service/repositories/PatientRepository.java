package com.hospital.patient_service.repositories;

import com.hospital.patient_service.dto.PatientDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PatientRepository extends CrudRepository<PatientDTO, String> {
    public PatientDTO findByCnp(String cnp);
}
