package com.example.beauty_salon_rest.service;

import static java.util.Objects.isNull;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.entity.UserRole;
import com.example.beauty_salon_rest.exception.UsernameNotFoundException;
import com.example.beauty_salon_rest.repository.UserRepository;
import com.example.beauty_salon_rest.web.dto.UserDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.web.dto.UserValidationResponseDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

//  public UserValidationResponseDto checkUserExistsWithMessage(UserValidationRequestDto dto) {
//
//    boolean exists = checkIfUserExists(dto);
//
//    String message = "";
//    if (exists) {
//      if (userRepository.existsByUsername(dto.getUsername())) {
//        message = "Това потребителско име е заето";
//      } else {
//        message = "Този email е вече регистриран";
//      }
//    }
//
//    log.info("Check user exists: username={}, email={}, exists={}",
//        dto.getUsername(), dto.getEmail(), exists);
//
//    return new UserValidationResponseDto(exists, message);
//  }

  public UserDto saveUser(UserDto dto) {

    UserValidationRequestDto validationDto = UserValidationRequestDto.builder()
        .username(dto.getUsername())
        .email(dto.getEmail())
        .build();
    Boolean validationResult = checkIfUserExists(validationDto);
    if (validationResult) {
      throw new RuntimeException("User already exists");
    }

    UserEntity userEntity = UserEntity.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .username(dto.getUsername())
        .password(dto.getPassword())
        .email(dto.getEmail())
        .phone(dto.getPhone())
        .userRole(dto.getUserRole())
        .active(Boolean.TRUE)
        .build();

    userRepository.save(userEntity);

    dto.setId(userEntity.getId());
    dto.setActive(userEntity.isActive());

    return dto;
  }

  public UserDto updateUser(UserDto dto) {

    if (isNull(dto.getId())) {
      throw new RuntimeException("Invalid User ID!");
    }

    UserEntity userEntity = userRepository.findById(dto.getId())
        .orElseThrow(() -> new RuntimeException("User with ID = " + dto.getId() + " not found"));

    String newEmail = dto.getEmail();
    boolean emailExist = userRepository.existsByEmail(newEmail);
    if (emailExist) {
      throw new RuntimeException("Email already exists");
    }

    userEntity.setPhone(dto.getPhone());
    userEntity.setFirstName(dto.getFirstName());
    userEntity.setLastName(dto.getLastName());
    userEntity.setEmail(dto.getEmail());

    userRepository.save(userEntity);
    return dto;
  }

  public UserDto changeStatus(UUID id) {

    UserEntity user = findEntityById(id);
    user.setActive(!user.isActive());

    return transformToDto(userRepository.save(user));
  }

  public UserDto changeUserRole(UUID id) {

    UserEntity user = findEntityById(id);

    if (user.getUserRole() == UserRole.USER) {
      user.setUserRole(UserRole.ADMIN);
    } else {
      user.setUserRole(UserRole.USER);
    }

    return transformToDto(userRepository.save(user));
  }

  public UserDto findById(UUID id) {

    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Потребител с това ID не е намерен: " + id));

    return transformToDto(userEntity);
  }

  public UserDto findByUsername(String username) {

    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Потребител с потребителско име "  + username + " не е намерен: "));

    return transformToDto(userEntity);
  }

  public UserEntity findEntityById(UUID id) {

    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Потребител с това ID не е намерен: " + id));
  }

  private UserDto transformToDto(UserEntity userEntity) {

    return UserDto.builder()
        .id(userEntity.getId())
        .password(userEntity.getPassword())
        .username(userEntity.getUsername())
        .firstName(userEntity.getFirstName())
        .lastName(userEntity.getLastName())
        .email(userEntity.getEmail())
        .phone(userEntity.getPhone())
        .userRole(userEntity.getUserRole())
        .active(userEntity.isActive())
        .build();
  }

  public List<UserDto> listAll() {

    return userRepository.findAll().stream().map(this::transformToDto).collect(Collectors.toList());
  }
}

