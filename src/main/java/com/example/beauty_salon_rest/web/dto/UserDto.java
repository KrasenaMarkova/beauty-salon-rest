package com.example.beauty_salon_rest.web.dto;

import com.example.beauty_salon_rest.entity.UserRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private UUID id;
  private String username;
  private String firstName;
  private String lastName;
  private UserRole userRole;
  private String email;
  private String phone;
  private String password;
  private boolean active;
}