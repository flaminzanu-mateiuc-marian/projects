package com.freightbroker.broker_service.service;



import com.freightbroker.broker_service.entity.ProviderDTO;

import java.util.List;
import java.util.Optional;

public interface ProviderService {
    public ProviderDTO save(ProviderDTO customer);

    public List<ProviderDTO> getAll();

    public void deleteById(Long id);

    public Optional<ProviderDTO> findById(Long id);

    public Optional<ProviderDTO> findByEmailAndPassword(String email, String password);
}
