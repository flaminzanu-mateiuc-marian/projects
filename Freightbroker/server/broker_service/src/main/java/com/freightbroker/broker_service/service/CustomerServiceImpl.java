package com.freightbroker.broker_service.service;

import com.freightbroker.broker_service.entity.CustomerDTO;
import com.freightbroker.broker_service.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
    public final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerDTO> getAll() {
        return (List<CustomerDTO>) customerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerDTO> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<CustomerDTO> findByEmailAndPassword(String email, String password) {
        Optional<CustomerDTO> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            if (customer.get().getParola().trim().equalsIgnoreCase(password.trim()))
                return customer;
        }
        return Optional.empty();
    }
}
