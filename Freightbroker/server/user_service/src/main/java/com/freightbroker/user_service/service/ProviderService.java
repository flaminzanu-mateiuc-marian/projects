package com.freightbroker.user_service.service;


import com.freightbroker.user_service.entity.CustomerDTO;
import com.freightbroker.user_service.entity.ProviderDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProviderService {
    public ProviderDTO save(ProviderDTO customer);

    public List<ProviderDTO> getAll();

    public void deleteById(Long id);

    public Optional<ProviderDTO> findById(Long id);
    public Optional<ProviderDTO> findByEmail(String email);
    public Optional<ProviderDTO> findByEmailAndPassword(String email, String password);

    public void sendCreatedNotification(ProviderDTO providerDTO) throws IOException;

}
