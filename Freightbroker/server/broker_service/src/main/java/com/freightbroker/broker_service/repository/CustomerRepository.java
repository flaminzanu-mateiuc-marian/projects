package com.freightbroker.broker_service.repository;

import com.freightbroker.broker_service.entity.CustomerDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerDTO, Long> {
    public Optional<CustomerDTO> findByEmail(String email);
}
