package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.dto.UserSyncDto;
import com.example.beauty_salon_rest.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

//  @Override
//  public boolean saveUser(UserSyncDto dto) {
//    if (userRepository.existsByEmail(dto.getEmail()) ||
//        userRepository.existsByUsername(dto.getUsername())) {
//      return false; // потребителят вече съществува
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
//    return true; // успешно записан
//  }

  @Override
  public boolean saveUser(UserSyncDto dto) {
      UserEntity user;

      if (dto.getId() != null && userRepository.existsById(dto.getId())) {
          // Update съществуващ потребител
          user = userRepository.findById(dto.getId()).get();
      } else if (userRepository.existsByEmail(dto.getEmail()) || userRepository.existsByUsername(dto.getUsername())) {
          return false; // потребителят вече съществува
      } else {
          // Създай нов потребител
          user = new UserEntity();
          user.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
      }

      user.setUsername(dto.getUsername());
      user.setEmail(dto.getEmail());
      user.setPhone(dto.getPhone());
      user.setPassword(dto.getPassword());
      user.setActive(dto.isActive());

      userRepository.save(user);

      return true; // успешно записан или обновен
  }

  @Override
  public UserEntity toggleUserStatus(UUID id) {
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не е намерен!"));

    user.setActive(!user.isActive());
    userRepository.save(user);

    return user;
  }



}

