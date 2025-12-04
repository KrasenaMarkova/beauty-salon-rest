package com.example.beauty_salon_rest.web;

import com.example.beauty_salon_rest.service.UserService;
import com.example.beauty_salon_rest.web.dto.StatusResponseDto;
import com.example.beauty_salon_rest.web.dto.UserDto;
import com.example.beauty_salon_rest.web.dto.UserRoleResponseDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.web.mapper.DtoMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/validation")
  public ResponseEntity<Boolean> checkUserExists(@RequestBody UserValidationRequestDto dto) {
    return ResponseEntity.ok(userService.checkIfUserExists(dto));
  }

  @PutMapping("/toggle-status/{id}")
  public ResponseEntity<StatusResponseDto> toggleUserStatus(@PathVariable UUID id) {
    UserDto userDto = userService.changeStatus(id);
    return ResponseEntity.ok(DtoMapper.from(userDto));
  }

  @PutMapping("/{id}/toggle-role")
  public ResponseEntity<UserRoleResponseDto> toggleUserRole(@PathVariable UUID id) {
    UserDto userDto = userService.changeUserRole(id);
    return ResponseEntity.ok(DtoMapper.mapRole(userDto));
  }

  @PostMapping
  public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
    return ResponseEntity.ok(userService.saveUser(dto));
  }

  @PutMapping
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto) {
    return ResponseEntity.ok(userService.updateUser(dto));
  }

  @GetMapping
  public ResponseEntity<UserDto> loadByUsername(@RequestParam String username) {
    return ResponseEntity.ok(userService.findByUsername(username));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> loadById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.listAll());
  }
}
