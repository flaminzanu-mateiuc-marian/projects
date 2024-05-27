package com.hospital.patient_service.services;

import com.hospital.patient_service.dto.PatientDTO;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    public Optional<PatientDTO> findByCnp(String cnp);

    public PatientDTO save(PatientDTO patient);

    public List<PatientDTO> getAll();

    public void deleteById(String id);
}