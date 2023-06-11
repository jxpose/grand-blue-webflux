package com.grandblue.webflux.services;

import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.ProductRequest;
import com.grandblue.webflux.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  private static final boolean STATE_DELETED = true;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Flux<ProductModel> getProduct() {
    return productRepository.findAll();
  }

  public Mono<ProductModel> getProduct(String productId) {
    return productRepository.findByProductId(UUID.fromString(productId));
  }

  public Mono<ProductModel> saveProduct(ProductRequest productRequest) {
    ProductModel product = new ProductModel(
        productRequest.productName(),
        productRequest.productDescription()
    );

    return productRepository.save(product);
  }

  public Mono<ProductModel> updateProduct(String productId, ProductRequest productRequest) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .flatMap(product -> {
          product.setProductName(productRequest.productName());
          product.setProductDescription(productRequest.productDescription());

          return productRepository.save(product);
        });
  }

  public Mono<ProductModel> deleteProduct(String productId) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .flatMap(product -> {
          product.setDeleteFlag(STATE_DELETED);
          return productRepository.save(product);
        });
  }
}
