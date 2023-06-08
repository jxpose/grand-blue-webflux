package com.grandblue.webflux.controllers;

import com.grandblue.webflux.common.validator.GrandBlueValidator;
import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.AddProductRequestModel;
import com.grandblue.webflux.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ProductRepository productRepository;
  private final GrandBlueValidator validator;

  public ProductController(ProductRepository productRepository, GrandBlueValidator validator) {
    this.productRepository = productRepository;
    this.validator = validator;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<ProductModel> getProducts() {
    return productRepository.findAll();
  }

  @GetMapping(value = "/{productId}")
  public Mono<ResponseEntity<ProductModel>> getProduct(@PathVariable String productId) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.badRequest().build());
  }

  @PostMapping
  public Mono<ResponseEntity<ProductModel>> saveProduct(@RequestBody AddProductRequestModel addProductRequestModel) {
    Errors errors = new BeanPropertyBindingResult(addProductRequestModel, "addProductRequestModel");

    return validator.validateAsync(addProductRequestModel, errors)
        .then(Mono.defer(() -> {
          if (errors.hasErrors()) {
            var error = errors.getFieldErrors().stream().map(FieldError::toString).collect(Collectors.joining("\n"));
            return Mono.error(new IllegalArgumentException(error));
          }

          ProductModel productModel = new ProductModel();
          productModel.generateProductId();
          productModel.setProductName(addProductRequestModel.productName());
          productModel.setProductDescription(addProductRequestModel.productDescription());

          return productRepository.save(productModel)
              .flatMap(savedProduct -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)));
        }))
        .onErrorResume(IllegalArgumentException.class, exception -> {
              logger.error(exception.getMessage());
              return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            }
        );
  }
}
