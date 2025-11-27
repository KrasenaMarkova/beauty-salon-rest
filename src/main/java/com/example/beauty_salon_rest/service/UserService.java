package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.dto.UserSyncDto;
import com.example.beauty_salon_rest.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.entity.UserEntity;
import java.util.UUID;
import org.apache.catalina.User;

public interface UserService {

  Boolean checkIfUserExists(UserValidationRequestDto dto);

  boolean saveUser(UserSyncDto dto);

  UserEntity toggleUserStatus(UUID id);
}