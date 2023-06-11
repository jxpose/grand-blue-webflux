package com.grandblue.webflux.exceptions;

public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
