package com.example.beauty_salon_rest.web;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ErrorResponse> handleException(Exception e) {
//
////    ErrorResponse dto = new ErrorResponse(LocalDateTime.now(),e.getMessage());
////
////    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
//  }

}
