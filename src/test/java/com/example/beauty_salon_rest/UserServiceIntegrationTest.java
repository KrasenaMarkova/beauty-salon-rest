package com.example.beauty_salon_rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.entity.UserRole;
import com.example.beauty_salon_rest.repository.UserRepository;
import com.example.beauty_salon_rest.service.UserService;
import com.example.beauty_salon_rest.web.dto.UserDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class UserServiceIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  void saveUser_shouldPersistUserInDatabase() {

    UserDto userDto = UserDto.builder()
        .id(UUID.randomUUID())
        .username("integrationUser")
        .firstName("Test")
        .lastName("User")
        .email("integration@test.com")
        .password("123456")
        .phone("0888123456")
        .userRole(UserRole.USER)
        .build();

    UserDto savedUser = userService.saveUser(userDto);

    Optional<UserEntity> userEntity = userRepository.findById(savedUser.getId());
    assertTrue(userEntity.isPresent());
    assertEquals("integrationUser", userEntity.get().getUsername());
  }
}
