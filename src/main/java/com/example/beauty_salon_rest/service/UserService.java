package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.repository.UserRepository;
import com.example.beauty_salon_rest.web.dto.UserSyncDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.web.dto.UserValidationResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Boolean checkIfUserExists(UserValidationRequestDto dto) {

    boolean existsByEmail = userRepository.existsByEmail(dto.getEmail());
    boolean existsByUsername = userRepository.existsByUsername(dto.getUsername());

    return existsByEmail || existsByUsername;
  }

  public UserValidationResponseDto checkUserExistsWithMessage(UserValidationRequestDto dto) {

    boolean exists = checkIfUserExists(dto);

    String message = "";
    if (exists) {
      if (userRepository.existsByUsername(dto.getUsername())) {
        message = "Това потребителско име е заето";
      } else {
        message = "Този email е вече регистриран";
      }
    }
//    else {
//      message = "Потребителят е наличен";
//    }

    log.info("Check user exists: username={}, email={}, exists={}",
        dto.getUsername(), dto.getEmail(), exists);

    return new UserValidationResponseDto(exists, message);
  }

  public boolean saveUser(UserSyncDto dto) {
    UserEntity user;

    if (dto.getId() != null && userRepository.existsById(dto.getId())) {
      user = userRepository.findById(dto.getId()).get();
    } else if (userRepository.existsByEmail(dto.getEmail()) || userRepository.existsByUsername(dto.getUsername())) {
      return false;
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
    return true;
  }

  public UserEntity changeStatus(UUID id) {
    UserEntity user = findById(id);
    user.setActive(!user.isActive());
    return userRepository.save(user);
  }

  public UserEntity findById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Потребител с това ID не е намерен: " + id));
  }

}

