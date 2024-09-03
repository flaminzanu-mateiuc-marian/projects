package com.freightbroker.user_service.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightbroker.user_service.entity.CustomerDTO;
import com.freightbroker.user_service.entity.MailDTO;
import com.freightbroker.user_service.entity.ProviderDTO;
import com.freightbroker.user_service.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static com.freightbroker.user_service.config.CorsConfig.getIp;

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
    public Optional<ProviderDTO> findByEmail(String email) {
        return providerRepository.findByEmail(email);
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
    @Override
    public void sendCreatedNotification(ProviderDTO provider) throws IOException {
        URL url = new URL("http://"+getIp()+":8083/api/freightbroker/sendMail");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String mailJson = objectMapper.writeValueAsString(new MailDTO(provider.getEmail(), "Cont creat pe platforma Freightbroker","Bun venit!"));
        byte[] mailBytes = mailJson.getBytes();

        var conOutputStream = con.getOutputStream();
        conOutputStream.write(mailBytes);
        con.getResponseCode();
    }
}
