package com.grandblue.webflux.common.validator;

import com.grandblue.webflux.models.requests.ValidatedRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
public class GrandBlueValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return ValidatedRequest.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidatedRequest request = (ValidatedRequest) target;
    request.validate(errors);
  }

  public Mono<Void> validateAsync(ValidatedRequest target, Errors errors) {
    validate(target, errors);
    return Mono.empty();
  }
}
