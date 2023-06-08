package com.grandblue.webflux.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("product")
public class ProductModel {

  @Id
  private Integer seq;

  @Column("product_id")
  private UUID productId;

  @Column("product_name")
  private String productName;

  @Column("product_description")
  private String productDescription;


  public UUID getProductId() {
    return productId;
  }

  public void setProductId(byte[] data) {
    this.productId = UUID.nameUUIDFromBytes(data);
  }

  public void generateProductId() {
    this.productId = UUID.randomUUID();
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }
}
