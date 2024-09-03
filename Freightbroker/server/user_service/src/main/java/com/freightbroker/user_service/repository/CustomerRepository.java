package com.freightbroker.user_service.repository;

import com.freightbroker.user_service.entity.CustomerDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerDTO, Long> {
    public Optional<CustomerDTO> findByEmail(String email);
}
