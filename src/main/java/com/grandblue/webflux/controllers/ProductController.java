package com.grandblue.webflux.controllers;

import com.grandblue.webflux.common.validator.GrandBlueValidator;
import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.ProductRequest;
import com.grandblue.webflux.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/products")
public class ProductController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ProductService productService;
  private final GrandBlueValidator validator;

  public ProductController(ProductService productService, GrandBlueValidator validator) {
    this.productService = productService;
    this.validator = validator;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<ProductModel> getProducts() {
    return productService.getProduct();
  }

  @GetMapping(value = "/{productId}")
  public Mono<ResponseEntity<ProductModel>> getProduct(@PathVariable String productId) {
    return productService.getProduct(productId)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.badRequest().build());
  }

  @PostMapping
  public Mono<ResponseEntity<ProductModel>> saveProduct(@RequestBody ProductRequest productRequest) {
    return validator.validateAsync(productRequest, productRequest.getClass().getSimpleName())
        .then(Mono.defer(() -> productService.saveProduct(productRequest)
            .map(savedProduct -> ResponseEntity.status(HttpStatus.CREATED).body(savedProduct))))
        .onErrorResume(GrandBlueValidator.ValidationException.class, exception -> {
          logValidationErrors(exception.getErrors());
          return Mono.just(ResponseEntity.badRequest().build());
        })
        .onErrorResume(Exception.class, exception -> {
          logger.error(exception.getMessage(), exception);
          return Mono.just(ResponseEntity.internalServerError().build());
        });
  }

  @PutMapping(value = "/{productId}")
  public Mono<ResponseEntity<ProductModel>> updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
    return validator.validateAsync(productRequest, productRequest.getClass().getSimpleName())
        .then(Mono.defer(() -> productService.updateProduct(productId, productRequest)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build())))
        .onErrorResume(GrandBlueValidator.ValidationException.class, exception -> {
          logValidationErrors(exception.getErrors());
          return Mono.just(ResponseEntity.badRequest().build());
        })
        .onErrorResume(Exception.class, exception -> {
          logger.error(exception.getMessage(), exception);
          return Mono.just(ResponseEntity.internalServerError().build());
        });
  }

  @DeleteMapping(value = "/{productId}")
  public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String productId) {
    return productService.deleteProduct(productId)
        .then(Mono.just(ResponseEntity.ok().<Void>build()))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  private void logValidationErrors(Errors errors) {
    logger.error("Validation error: " + errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining("\n")));
  }
}
