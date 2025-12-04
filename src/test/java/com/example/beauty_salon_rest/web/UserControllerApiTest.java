package com.example.beauty_salon_rest.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.beauty_salon_rest.entity.UserRole;
import com.example.beauty_salon_rest.service.UserService;
import com.example.beauty_salon_rest.web.dto.UserDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

  @MockitoBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void postCheckUserExists_shouldReturnOkAndBoolean() throws Exception {
    UserValidationRequestDto validationDto = UserValidationRequestDto.builder()
        .username("existinguser")
        .email("test@example.com")
        .build();

    // Mockito mock
    when(userService.checkIfUserExists(org.mockito.ArgumentMatchers.any(UserValidationRequestDto.class)))
        .thenReturn(true);

    mockMvc.perform(post("/api/v1/users/validation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validationDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  void putToggleUserStatus_shouldReturnOkAndStatusResponse() throws Exception {
    UUID userId = UUID.randomUUID();

    UserDto mockUser = UserDto.builder()
        .id(userId)
        .username("testuser")
        .active(true)
        .build();

    when(userService.changeStatus(userId)).thenReturn(mockUser);

    mockMvc.perform(put("/api/v1/users/toggle-status/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  void putToggleUserRole_shouldReturnOkAndUserRoleResponse() throws Exception {
    UUID userId = UUID.randomUUID();

    UserDto mockUser = UserDto.builder()
        .id(userId)
        .username("testuser")
        .userRole(UserRole.USER) // текуща роля
        .build();

    when(userService.changeUserRole(userId)).thenReturn(mockUser);

    mockMvc.perform(put("/api/v1/users/{id}/toggle-role", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.role").value("USER"));
  }

  @Test
  void postSaveUser_shouldReturnOkAndUserDto() throws Exception {
    UUID userId = UUID.randomUUID();

    UserDto inputUser = UserDto.builder()
        .username("newuser")
        .active(true)
        .build();

    UserDto savedUser = UserDto.builder()
        .id(userId)
        .username("newuser")
        .active(true)
        .build();

    when(userService.saveUser(org.mockito.ArgumentMatchers.any(UserDto.class)))
        .thenReturn(savedUser);

    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("newuser"))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  void putUpdateUser_shouldReturnOkAndUserDto() throws Exception {
    UUID userId = UUID.randomUUID();

    UserDto inputUser = UserDto.builder()
        .id(userId)
        .username("updateduser")
        .active(true)
        .build();

    UserDto updatedUser = UserDto.builder()
        .id(userId)
        .username("updateduser")
        .active(true)
        .build();

    // Mockito mock - използваме any(UserDto.class), защото JSON сериализацията създава нов обект
    when(userService.updateUser(org.mockito.ArgumentMatchers.any(UserDto.class)))
        .thenReturn(updatedUser);

    mockMvc.perform(put("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("updateduser"))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  void getLoadByUsername_shouldReturnOkAndUserDto() throws Exception {
    String username = "testuser";
    UUID userId = UUID.randomUUID();

    UserDto mockUser = UserDto.builder()
        .id(userId)
        .username(username)
        .active(true)
        .build();

    // Mockito mock
    when(userService.findByUsername(username)).thenReturn(mockUser);

    mockMvc.perform(get("/api/v1/users")
            .param("username", username)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  void getLoadById_shouldReturnOkAndUserDto() throws Exception {
    UUID userId = UUID.randomUUID();
    String username = "testuser";

    UserDto mockUser = UserDto.builder()
        .id(userId)
        .username(username)
        .active(true)
        .build();

    // Mockito mock
    when(userService.findById(userId)).thenReturn(mockUser);

    mockMvc.perform(get("/api/v1/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value(username))
        .andExpect(jsonPath("$.active").value(true));
  }

  @Test
  void getFindAll_shouldReturnOkAndListOfUsers() throws Exception {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    List<UserDto> mockUsers = List.of(
        UserDto.builder().id(userId1).username("user1").active(true).build(),
        UserDto.builder().id(userId2).username("user2").active(false).build()
    );

    // Mockito mock
    when(userService.listAll()).thenReturn(mockUsers);

    mockMvc.perform(get("/api/v1/users/list")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(userId1.toString()))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[0].active").value(true))
        .andExpect(jsonPath("$[1].id").value(userId2.toString()))
        .andExpect(jsonPath("$[1].username").value("user2"))
        .andExpect(jsonPath("$[1].active").value(false));
  }

}
