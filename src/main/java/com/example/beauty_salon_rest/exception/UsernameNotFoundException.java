package com.example.beauty_salon_rest.exception;

public class UsernameNotFoundException extends RuntimeException {

  public UsernameNotFoundException(String message) {
    super(message);
  }
}
