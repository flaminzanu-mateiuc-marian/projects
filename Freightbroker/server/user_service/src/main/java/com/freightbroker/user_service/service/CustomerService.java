package com.freightbroker.user_service.service;


import com.freightbroker.user_service.entity.CustomerDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public CustomerDTO save(CustomerDTO customer);

    public List<CustomerDTO> getAll();

    public void deleteById(Long id);

    public Optional<CustomerDTO> findById(Long id);

    public Optional<CustomerDTO> findByEmailAndPassword(String email, String password);
    public Optional<CustomerDTO> findByEmail(String email);
    public void sendCreatedNotification(CustomerDTO customer) throws IOException;
}



