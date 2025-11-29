package com.example.beauty_salon_rest.web.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private LocalDateTime timestamp;

  private String message;

  public ErrorResponse(LocalDateTime timestamp, String message) {
    this.timestamp = timestamp;
    this.message = message;
  }

}
