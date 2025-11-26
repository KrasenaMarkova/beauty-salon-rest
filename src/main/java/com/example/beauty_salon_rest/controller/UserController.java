package com.example.beauty_salon_rest.controller;

import com.example.beauty_salon_rest.dto.UserSyncDto;
import com.example.beauty_salon_rest.dto.UserValidationRequestDto;
import com.example.beauty_salon_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

//  @PostMapping("/sync")
//  public ResponseEntity<Void> syncUser(@RequestBody UserSyncDto dto) {
//    userService.saveUser(dto);
//    return ResponseEntity.ok().build();
//  }

  @PostMapping("/sync")
  public ResponseEntity<String> syncUser(@RequestBody UserSyncDto dto) {
    boolean success = userService.saveUser(dto);

    if (success) {
      return ResponseEntity.ok("User synced successfully!");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("User already exists!");
    }
  }

}
