package com.example.beauty_salon_rest.web.controller;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.repository.UserRepository;
import com.example.beauty_salon_rest.service.UserService;
import com.example.beauty_salon_rest.web.dto.StatusResponseDto;
import com.example.beauty_salon_rest.web.dto.UserSyncDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.web.dto.UserValidationResponseDto;
import com.example.beauty_salon_rest.web.mapper.DtoMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserRepository userRepository;

  @PostMapping("/validation")
  public ResponseEntity<Boolean> checkUserExists(@RequestBody UserValidationRequestDto dto) {

    UserValidationResponseDto response = userService.checkUserExistsWithMessage(dto);

    return ResponseEntity.ok(userService.checkIfUserExists(dto));
  }

  @PostMapping("/sync")
  public ResponseEntity<String> syncUser(@RequestBody UserSyncDto dto) {
    boolean success = userService.saveUser(dto);

    if (success) {
      return ResponseEntity.ok("User synced successfully!");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("User already exists!");
    }
  }

  @PutMapping("/toggle-status/{id}")
  public ResponseEntity<StatusResponseDto> toggleUserStatus(@PathVariable UUID id) {
    UserEntity updatedUser = userService.changeStatus(id);
    return ResponseEntity.ok(DtoMapper.from(updatedUser));
  }

  @GetMapping
  public List<UserEntity> getAllUsers() {
    return userRepository.findAll();
  }

}
