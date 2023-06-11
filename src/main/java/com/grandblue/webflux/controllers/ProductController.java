package com.grandblue.webflux.controllers;

import com.grandblue.webflux.common.validator.GrandBlueValidator;
import com.grandblue.webflux.exceptions.DataNotFoundException;
import com.grandblue.webflux.models.requests.ProductRequest;
import com.grandblue.webflux.models.response.GrandBlueResponse;
import com.grandblue.webflux.models.response.ProductResponse;
import com.grandblue.webflux.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


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

  @GetMapping
  public Mono<ResponseEntity<GrandBlueResponse<ProductResponse>>> getProducts() {
    return productService.getProduct()
        .collectList()
        .map(GrandBlueResponse::new)
        .map(ResponseEntity::ok);
  }

  @GetMapping(value = "/{productId}")
  public Mono<ResponseEntity<GrandBlueResponse<ProductResponse>>> getProduct(@PathVariable String productId) {
    return productService.getProduct(productId)
        .map(GrandBlueResponse::new)
        .map(ResponseEntity::ok)
        .onErrorResume(DataNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()));
  }

  @PostMapping
  public Mono<ResponseEntity<GrandBlueResponse<ProductResponse>>> saveProduct(@RequestBody ProductRequest productRequest) {
    return validator.validateAsync(productRequest, productRequest.getClass().getSimpleName())
        .then(Mono.defer(() -> productService.saveProduct(productRequest)
            .map(GrandBlueResponse::new)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))))
        .onErrorResume(GrandBlueValidator.ValidationException.class, exception -> {

          GrandBlueResponse<ProductResponse> response = GrandBlueResponse.error(
              exception.getErrors()
                  .getAllErrors()
                  .stream()
                  .map(ObjectError::toString)
                  .toList()
          );
          return Mono.just(ResponseEntity.badRequest().body(response));
        })
        .onErrorResume(Exception.class, exception -> {
          logger.error(exception.getMessage(), exception);
          return Mono.just(ResponseEntity.internalServerError().build());
        });
  }

  @PutMapping(value = "/{productId}")
  public Mono<ResponseEntity<GrandBlueResponse<ProductResponse>>> updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
    return validator.validateAsync(productRequest, productRequest.getClass().getSimpleName())
        .then(Mono.defer(() -> productService.updateProduct(productId, productRequest)
            .map(GrandBlueResponse::new)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build())))
        .onErrorResume(GrandBlueValidator.ValidationException.class, exception -> {
          GrandBlueResponse<ProductResponse> response = GrandBlueResponse.error(
              exception.getErrors()
                  .getAllErrors()
                  .stream()
                  .map(ObjectError::toString)
                  .toList()
          );
          return Mono.just(ResponseEntity.badRequest().body(response));
        })
        .onErrorResume(DataNotFoundException.class, exception -> Mono.just(ResponseEntity.notFound().build()))
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
}
