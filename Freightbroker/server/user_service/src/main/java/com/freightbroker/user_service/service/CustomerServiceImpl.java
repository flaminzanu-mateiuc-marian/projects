package com.freightbroker.user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightbroker.user_service.entity.CustomerDTO;
import com.freightbroker.user_service.entity.MailDTO;
import com.freightbroker.user_service.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static com.freightbroker.user_service.config.CorsConfig.getIp;

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

    @Override
    public Optional<CustomerDTO> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void sendCreatedNotification(CustomerDTO customer) throws IOException {
        URL url = new URL("http://"+getIp()+":8083/api/freightbroker/sendMail");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String mailJson = objectMapper.writeValueAsString(new MailDTO(customer.getEmail(), "Cont creat pe platforma Freightbroker","Bun venit!"));
        byte[] mailBytes = mailJson.getBytes();

        var conOutputStream = con.getOutputStream();
        conOutputStream.write(mailBytes);
        con.getResponseCode();
    }
}
