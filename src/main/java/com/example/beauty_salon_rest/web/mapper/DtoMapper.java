package com.example.beauty_salon_rest.web.mapper;

import com.example.beauty_salon_rest.web.dto.StatusResponseDto;
import com.example.beauty_salon_rest.web.dto.UserDto;
import com.example.beauty_salon_rest.web.dto.UserRoleResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

  public static StatusResponseDto from(UserDto userDto) {

    return StatusResponseDto.builder()
        .id(userDto.getId())
        .active(userDto.isActive())
        .build();
  }

  public static UserRoleResponseDto mapRole(UserDto userDto) {
    return UserRoleResponseDto.builder()
        .id(userDto.getId())
        .role(userDto.getUserRole())
        .build();
  }
}
