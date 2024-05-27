package com.hospital.physician_service.services;

import com.hospital.physician_service.dto.PhysicianDTO;
import com.hospital.physician_service.repositories.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhysicianServiceImpl implements PhysicianService {
    @Autowired
    private PhysicianRepository physicianRepository;

    @Override
    public PhysicianDTO save(PhysicianDTO physician) {
        physicianRepository.save(physician);
        return physician;
    }

    @Override
    public List<PhysicianDTO> getAll() {
        return (List<PhysicianDTO>) physicianRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        physicianRepository.deleteById(id);
    }

    @Override
    public Optional<PhysicianDTO> findById(Long id) {
        return physicianRepository.findById(id);
    }

    @Override
    public List<PhysicianDTO> getBySpecialization(String specialization) {
        return getAll().stream()
                .filter(physician -> physician.getSpecializare().trim().equalsIgnoreCase(specialization.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhysicianDTO> searchByName(String name) {
        return getAll()
                .stream()
                .filter(physician -> physician.getNume().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }


}
