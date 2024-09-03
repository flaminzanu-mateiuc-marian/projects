package com.freightbroker.broker_service.service;
import com.freightbroker.broker_service.entity.ProviderDTO;
import com.freightbroker.broker_service.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService{
    public final ProviderRepository providerRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public ProviderDTO save(ProviderDTO customer) {
        return providerRepository.save(customer);
    }

    @Override
    public List<ProviderDTO> getAll() {
        return (List<ProviderDTO>) providerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        providerRepository.deleteById(id);
    }

    @Override
    public Optional<ProviderDTO> findById(Long id) {
        return providerRepository.findById(id);
    }

    @Override
    public Optional<ProviderDTO> findByEmailAndPassword(String email, String password){
        Optional<ProviderDTO> customer = providerRepository.findByEmail(email);
        if(customer.isPresent()){
            if(customer.get().getParola().trim().equalsIgnoreCase(password.trim()))
                return customer;
        }
        return Optional.empty();
    }

}
