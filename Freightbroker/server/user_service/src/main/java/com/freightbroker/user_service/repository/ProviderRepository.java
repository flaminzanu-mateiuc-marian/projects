package com.freightbroker.user_service.repository;

import com.freightbroker.user_service.entity.ProviderDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProviderRepository extends CrudRepository<ProviderDTO, Long> {
    public Optional<ProviderDTO> findByEmail(String email);
}
