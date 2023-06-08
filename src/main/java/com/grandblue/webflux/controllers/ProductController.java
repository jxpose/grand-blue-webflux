package com.grandblue.webflux.controllers;

import com.grandblue.webflux.models.ProductModel;
import com.grandblue.webflux.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
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
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ProductModel> saveProduct(@RequestBody ProductModel productModel) {
    return productRepository.save(productModel);
  }
}
