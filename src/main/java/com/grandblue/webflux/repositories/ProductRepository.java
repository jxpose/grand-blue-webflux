package com.grandblue.webflux.repositories;

import com.grandblue.webflux.models.ProductModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<ProductModel, Integer> {

  @Query("SELECT * FROM product WHERE product_id :productId")
  Mono<ProductModel> findByProductId(UUID productId);
}
