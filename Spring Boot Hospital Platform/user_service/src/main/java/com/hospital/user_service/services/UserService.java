package com.hospital.user_service.services;

import com.hospital.user_service.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserDTO save(UserDTO physician);

    public void deleteById(Long id);

    public Optional<UserDTO> findById(Long id);

    public Optional<UserDTO> findByUsernameAndPassword(String username, String password);

}