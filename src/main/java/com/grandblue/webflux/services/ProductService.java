package com.grandblue.webflux.services;

import com.grandblue.webflux.models.db.ProductModel;
import com.grandblue.webflux.models.requests.ProductRequest;
import com.grandblue.webflux.models.response.ProductResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface ProductService {

  public Flux<ProductResponse> getProduct();

  public Mono<ProductResponse> getProduct(String productId);

  public Mono<ProductResponse> saveProduct(ProductRequest productRequest);

  public Mono<ProductResponse> updateProduct(String productId, ProductRequest productRequest);

  public Mono<ProductModel> deleteProduct(String productId);
}
