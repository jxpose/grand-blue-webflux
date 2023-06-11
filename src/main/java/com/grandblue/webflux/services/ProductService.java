package com.grandblue.webflux.services;

import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.ProductRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface ProductService {

  public Flux<ProductModel> getProduct();

  public Mono<ProductModel> getProduct(String productId);

  public Mono<ProductModel> saveProduct(ProductRequest productRequest);

  public Mono<ProductModel> updateProduct(String productId, ProductRequest productRequest);

  public Mono<ProductModel> deleteProduct(String productId);
}
