package com.example.beauty_salon_rest.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.entity.UserRole;
import com.example.beauty_salon_rest.exception.UsernameNotFoundException;
import com.example.beauty_salon_rest.repository.UserRepository;
import com.example.beauty_salon_rest.service.UserService;
import com.example.beauty_salon_rest.web.dto.UserDto;
import com.example.beauty_salon_rest.web.dto.UserValidationRequestDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void whenCheckIfUserExists_andEmailOrUsernameExists_thenReturnTrue() {

    UserValidationRequestDto dto = UserValidationRequestDto.builder()
        .email("test@example.com")
        .username("tester")
        .build();

    when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
    when(userRepository.existsByUsername("tester")).thenReturn(false);

    Boolean result = userService.checkIfUserExists(dto);

    assertTrue(result);
  }

  @Test
  void whenCheckIfUserExists_andBothEmailAndUsernameDoNotExist_thenReturnFalse() {

    UserValidationRequestDto dto = UserValidationRequestDto.builder()
        .email("new@example.com")
        .username("newuser")
        .build();

    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(userRepository.existsByUsername("newuser")).thenReturn(false);

    Boolean result = userService.checkIfUserExists(dto);

    assertFalse(result);
  }

  @Test
  void whenSaveUser_andUserAlreadyExists_thenThrowException() {

    UserDto dto = UserDto.builder()
        .firstName("Gosho")
        .lastName("Petrov")
        .username("gosho")
        .email("gosho@mail.com")
        .password("1234")
        .build();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
    when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.saveUser(dto));
  }

  @Test
  void whenSaveUser_andUserDoesNotExist_thenSaveAndReturnDto() {

    UserDto dto = UserDto.builder()
        .firstName("Mira")
        .lastName("Ivanova")
        .username("mira")
        .email("mira@mail.com")
        .password("pass")
        .phone("0888123456")
        .userRole(UserRole.valueOf("USER"))
        .build();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);

    UserEntity savedUser = UserEntity.builder()
        .id(UUID.randomUUID())
        .firstName("Mira")
        .lastName("Ivanova")
        .username("mira")
        .password("pass")
        .email("mira@mail.com")
        .phone("0888123456")
        .userRole(UserRole.valueOf("USER"))
        .active(true)
        .build();

    when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

    UserDto result = userService.saveUser(dto);

    assertTrue(result.isActive());
    assertEquals("mira", result.getUsername());
    assertEquals("Mira", result.getFirstName());
    assertEquals("Ivanova", result.getLastName());

    verify(userRepository).save(any(UserEntity.class));
  }

  @Test
  void whenUpdateUserWithNullId_thenThrowsException() {
    UserDto dto = UserDto.builder()
        .id(null)
        .firstName("Test")
        .lastName("User")
        .email("test@example.com")
        .build();

    assertThrows(RuntimeException.class, () -> userService.updateUser(dto),
        "Invalid User ID!");
  }

  @Test
  void whenUpdateUserWithNonExistingUser_thenThrowsException() {
    UUID userId = UUID.randomUUID();
    UserDto dto = UserDto.builder()
        .id(userId)
        .firstName("Test")
        .lastName("User")
        .email("test@example.com")
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(dto));
    assertEquals("User with ID = " + userId + " not found", ex.getMessage());
  }

  @Test
  void whenUpdateUserWithExistingEmail_thenThrowsException() {
    UUID userId = UUID.randomUUID();
    UserDto dto = UserDto.builder()
        .id(userId)
        .firstName("Test")
        .lastName("User")
        .email("existing@example.com")
        .build();

    UserEntity existingUser = new UserEntity();
    existingUser.setId(userId);
    existingUser.setEmail("different@example.com");

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

    RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(dto));
    assertEquals("Email already exists", ex.getMessage());
  }

  @Test
  void whenUpdateUserWithValidData_thenUpdatesAndSavesUser() {
    UUID userId = UUID.randomUUID();
    UserDto dto = UserDto.builder()
        .id(userId)
        .firstName("NewFirst")
        .lastName("NewLast")
        .email("new@example.com")
        .phone("123456")
        .build();

    UserEntity existingUser = new UserEntity();
    existingUser.setId(userId);
    existingUser.setEmail("old@example.com");

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(userRepository.save(existingUser)).thenReturn(existingUser);

    UserDto result = userService.updateUser(dto);

    assertEquals("NewFirst", existingUser.getFirstName());
    assertEquals("NewLast", existingUser.getLastName());
    assertEquals("new@example.com", existingUser.getEmail());
    assertEquals("123456", existingUser.getPhone());
    assertEquals(dto, result);

    verify(userRepository).save(existingUser);
  }

  @Test
  void whenChangeStatus_thenTogglesActiveStatus() {
    UUID userId = UUID.randomUUID();
    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setActive(false);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    UserDto result = userService.changeStatus(userId);

    assertTrue(user.isActive());
    assertEquals(user.getId(), result.getId());
    verify(userRepository).save(user);
  }

  @Test
  void whenChangeUserRoleFromUserToAdmin_thenRoleIsUpdated() {
    UUID userId = UUID.randomUUID();
    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setUserRole(UserRole.USER);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    UserDto result = userService.changeUserRole(userId);

    assertEquals(UserRole.ADMIN, user.getUserRole());
    assertEquals(user.getId(), result.getId());
    verify(userRepository).save(user);
  }

  @Test
  void whenChangeUserRoleFromAdminToUser_thenRoleIsUpdated() {
    UUID userId = UUID.randomUUID();
    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setUserRole(UserRole.ADMIN);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    UserDto result = userService.changeUserRole(userId);

    assertEquals(UserRole.USER, user.getUserRole());
    assertEquals(user.getId(), result.getId());
    verify(userRepository).save(user);
  }

  @Test
  void whenFindByIdAndUserExists_thenReturnsUserDto() {
    UUID userId = UUID.randomUUID();
    UserEntity userEntity = UserEntity.builder()
        .id(userId)
        .firstName("Gosho")
        .lastName("Petrov")
        .username("gosho123")
        .email("gosho@example.com")
        .active(true)
        .userRole(UserRole.USER)
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

    UserDto result = userService.findById(userId);

    assertEquals(userId, result.getId());
    assertEquals("Gosho", result.getFirstName());
    assertEquals("Petrov", result.getLastName());
    assertEquals("gosho123", result.getUsername());
    assertTrue(result.isActive());
    assertEquals(UserRole.USER, result.getUserRole());
  }

  @Test
  void whenFindByIdAndUserDoesNotExist_thenThrowsException() {
    UUID userId = UUID.randomUUID();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findById(userId));
    assertEquals("Потребител с това ID не е намерен: " + userId, ex.getMessage());
  }

  @Test
  void whenFindByUsernameAndUserExists_thenReturnsUserDto() {
    String username = "gosho123";
    UserEntity userEntity = UserEntity.builder()
        .id(UUID.randomUUID())
        .username(username)
        .firstName("Gosho")
        .lastName("Petrov")
        .email("gosho@example.com")
        .active(true)
        .userRole(UserRole.USER)
        .build();

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

    UserDto result = userService.findByUsername(username);

    assertEquals(username, result.getUsername());
    assertEquals("Gosho", result.getFirstName());
    assertEquals("Petrov", result.getLastName());
  }

  @Test
  void whenFindByUsernameAndUserDoesNotExist_thenThrowsUsernameNotFoundException() {
    String username = "unknown";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
        () -> userService.findByUsername(username));

    assertEquals("Потребител с потребителско име " + username + " не е намерен: ", ex.getMessage());
  }

  @Test
  void whenFindEntityByIdAndUserExists_thenReturnsUserEntity() {
    UUID userId = UUID.randomUUID();
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

    UserEntity result = userService.findEntityById(userId);

    assertEquals(userId, result.getId());
  }

  @Test
  void whenFindEntityByIdAndUserDoesNotExist_thenThrowsException() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findEntityById(userId));
    assertEquals("Потребител с това ID не е намерен: " + userId, ex.getMessage());
  }

  @Test
  void whenListAll_thenReturnsAllUsersAsDto() {
    UserEntity user1 = UserEntity.builder()
        .id(UUID.randomUUID())
        .firstName("Gosho")
        .lastName("Petrov")
        .username("gosho123")
        .email("gosho@example.com")
        .active(true)
        .userRole(UserRole.USER)
        .build();

    UserEntity user2 = UserEntity.builder()
        .id(UUID.randomUUID())
        .firstName("Pesho")
        .lastName("Ivanov")
        .username("pesho456")
        .email("pesho@example.com")
        .active(false)
        .userRole(UserRole.ADMIN)
        .build();

    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    List<UserDto> result = userService.listAll();

    assertEquals(2, result.size());
    assertEquals("Gosho", result.get(0).getFirstName());
    assertEquals("Pesho", result.get(1).getFirstName());
  }
}
