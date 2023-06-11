package com.grandblue.webflux.services.impl;

import com.grandblue.webflux.exceptions.DataNotFoundException;
import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.ProductRequest;
import com.grandblue.webflux.models.response.ProductResponse;
import com.grandblue.webflux.repositories.ProductRepository;
import com.grandblue.webflux.services.ProductService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private static final boolean STATE_DELETED = true;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Flux<ProductResponse> getProduct() {
    return productRepository.findAll().map(ProductResponse::new);
  }

  @Override
  public Mono<ProductResponse> getProduct(String productId) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .switchIfEmpty(Mono.error(new DataNotFoundException("Product does not exist")))
        .map(ProductResponse::new);
  }

  @Override
  public Mono<ProductResponse> saveProduct(ProductRequest productRequest) {
    ProductModel product = new ProductModel(
        productRequest.productName(),
        productRequest.productDescription()
    );

    return productRepository.save(product).map(ProductResponse::new);
  }

  @Override
  public Mono<ProductResponse> updateProduct(String productId, ProductRequest productRequest) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .switchIfEmpty(Mono.error(new DataNotFoundException("Product does not exist")))
        .flatMap(product -> {
          product.setProductName(productRequest.productName());
          product.setProductDescription(productRequest.productDescription());

          return productRepository.save(product).map(ProductResponse::new);
        });
  }

  @Override
  public Mono<ProductModel> deleteProduct(String productId) {
    return productRepository.findByProductId(UUID.fromString(productId))
        .flatMap(product -> {
          product.setDeleteFlag(STATE_DELETED);
          return productRepository.save(product);
        });
  }
}
