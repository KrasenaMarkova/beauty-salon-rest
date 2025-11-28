package com.example.beauty_salon_rest.web.dto;

import com.example.beauty_salon_rest.entity.UserRole;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleResponseDto {

  private UUID id;
  private UserRole role;

}
