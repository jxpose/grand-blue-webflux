package com.grandblue.webflux.models.requests;

import org.springframework.validation.Errors;

public interface ValidatedRequest {
  void validate(Errors errors);
}
