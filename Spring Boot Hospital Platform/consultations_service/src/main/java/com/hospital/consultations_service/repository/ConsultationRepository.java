package com.hospital.consultations_service.repository;

import com.hospital.consultations_service.dto.ConsultationDTO;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConsultationRepository extends MongoRepository<ConsultationDTO, String> {

}