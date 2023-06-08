package com.grandblue.webflux.models.requests;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public record AddProductRequestModel(String productName, String productDescription) implements ValidatedRequest {

  @Override
  public void validate(Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productName", "required", "Product name is required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productDescription", "required", "Product description is required");
  }
}
