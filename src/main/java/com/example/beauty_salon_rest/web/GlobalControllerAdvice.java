package com.example.beauty_salon_rest.web;

import com.example.beauty_salon_rest.exception.UsernameNotFoundException;
import com.example.beauty_salon_rest.web.dto.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {

    ErrorResponse dto = new ErrorResponse(LocalDateTime.now(), e.getMessage());

    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(dto);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {

    ErrorResponse dto = new ErrorResponse(LocalDateTime.now(),e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
  }

}
