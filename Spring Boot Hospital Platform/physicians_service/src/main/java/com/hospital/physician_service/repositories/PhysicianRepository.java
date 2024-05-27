package com.hospital.physician_service.repositories;

import com.hospital.physician_service.dto.PhysicianDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicianRepository extends CrudRepository<PhysicianDTO, Long> {
}
