package com.example.beauty_salon_rest.web.mapper;

import com.example.beauty_salon_rest.entity.UserEntity;
import com.example.beauty_salon_rest.web.dto.StatusResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

  public static StatusResponseDto from(UserEntity userEntity) {

    return StatusResponseDto.builder()
        .id(userEntity.getId())
        .active(userEntity.isActive())
        .build();
  }


}
