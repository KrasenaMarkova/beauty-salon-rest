package com.example.beauty_salon_rest.service;

import com.example.beauty_salon_rest.dto.UserSyncDto;
import com.example.beauty_salon_rest.dto.UserValidationRequestDto;

public interface UserService {

  Boolean checkIfUserExists(UserValidationRequestDto dto);

  boolean saveUser(UserSyncDto dto);
}