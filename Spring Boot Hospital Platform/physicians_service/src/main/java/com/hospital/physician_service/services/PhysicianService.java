package com.hospital.physician_service.services;

import com.hospital.physician_service.dto.PhysicianDTO;

import java.util.List;
import java.util.Optional;

public interface PhysicianService {
    public PhysicianDTO save(PhysicianDTO physician);

    public List<PhysicianDTO> getAll();

    public void deleteById(Long id);

    public Optional<PhysicianDTO> findById(Long id);

    public List<PhysicianDTO> getBySpecialization(String specialization);

    public List<PhysicianDTO> searchByName(String name);
}