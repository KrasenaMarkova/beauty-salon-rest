package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.dto.UserSyncDto;
import com.example.beauty_salon_rest.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public Boolean checkIfUserExists(UserValidationRequestDto dto) {

    boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());
    boolean existsByUsername = userRepository.existsByUsername(dto.getUsername());

    return existsByEmail || existsByUsername;

  }

//  @Override
//  public void saveUser(UserSyncDto dto) {
//    if (userRepository.existsByEmail(dto.getEmail()) ||
//        userRepository.existsByUsername(dto.getUsername())) {
//      throw new RuntimeException("User already exists in microservice DB!");
//    }
//
//    UserEntity user = new UserEntity();
//    user.setUsername(dto.getUsername());
//    user.setEmail(dto.getEmail());
//    user.setPhone(dto.getPhone());
//    user.setPassword(dto.getPassword());
//    user.setActive(true);
//    userRepository.save(user);
//
//  }

  @Override
  public boolean saveUser(UserSyncDto dto) {
    if (userRepository.existsByEmail(dto.getEmail()) ||
        userRepository.existsByUsername(dto.getUsername())) {
      return false; // потребителят вече съществува
    }

    UserEntity user = new UserEntity();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPhone(dto.getPhone());
    user.setPassword(dto.getPassword());
    user.setActive(true);
    userRepository.save(user);

    return true; // успешно записан
  }


}

