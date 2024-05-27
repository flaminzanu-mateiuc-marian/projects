package com.hospital.investigations_service.repository;
import com.hospital.investigations_service.dto.InvestigationsDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvestigationsRepository extends MongoRepository<InvestigationsDTO, String> {

}

