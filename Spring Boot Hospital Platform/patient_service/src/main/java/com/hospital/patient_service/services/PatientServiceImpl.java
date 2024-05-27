package com.hospital.patient_service.services;

import com.hospital.patient_service.dto.PatientDTO;
import com.hospital.patient_service.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public List<PatientDTO> findAll() {
        return (List<PatientDTO>) patientRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Optional<PatientDTO> findByCnp(String cnp) {
        return Optional.ofNullable(patientRepository.findByCnp(cnp));
    }

    @Override
    public PatientDTO save(PatientDTO patient) {
        patientRepository.save(patient);
        return patient;
    }

    @Override
    public List<PatientDTO> getAll() {
        List<PatientDTO> list = (List<PatientDTO>) patientRepository.findAll();
        return list;
    }
}
