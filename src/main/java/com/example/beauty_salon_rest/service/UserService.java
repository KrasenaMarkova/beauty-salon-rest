package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.web.dto.StatusRequestDto;
import com.example.beauty_salon_rest.web.dto.UserSyncDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

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

  public boolean saveUser(UserSyncDto dto) {
    UserEntity user;

    if (dto.getId() != null && userRepository.existsById(dto.getId())) {
      user = userRepository.findById(dto.getId()).get();
    } else if (userRepository.existsByEmail(dto.getEmail()) || userRepository.existsByUsername(dto.getUsername())) {
      return false; // потребителят вече съществува
    } else {
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

//  @Override
//  public UserEntity toggleUserStatus(UUID id) {
//    UserEntity user = userRepository.findById(id)
//        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Потребителят не е намерен!"));
//
//    user.setActive(!user.isActive());
//    userRepository.save(user);
//
//    return user;
//  }


  public UserEntity changeStatus(StatusRequestDto request) {
    Optional<UserEntity> user = userRepository.findById(request.getId());

    if (!user.get().isActive()) {
      user.get().setActive(!request.isActive());
    } else {
      user.get().setActive(request.isActive());
    }

    return userRepository.save(user.get());
  }


}

