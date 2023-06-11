package com.grandblue.webflux.repositories;

import com.grandblue.webflux.models.db.ProductModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<ProductModel, Integer> {

  @Query("SELECT * FROM product WHERE delete_flag = 0")
  Flux<ProductModel> findAll();

  @Query("SELECT * FROM product WHERE product_id = :productId AND delete_flag = 0")
  Mono<ProductModel> findByProductId(@Param("productId") UUID productId);
}
