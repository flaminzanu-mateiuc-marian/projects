package com.freightbroker.broker_service.service;


import com.freightbroker.broker_service.entity.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public CustomerDTO save(CustomerDTO customer);
    public List<CustomerDTO> getAll();
    public void deleteById(Long id);

    public Optional<CustomerDTO> findById(Long id);

    public Optional<CustomerDTO> findByEmailAndPassword(String email, String password);
}
