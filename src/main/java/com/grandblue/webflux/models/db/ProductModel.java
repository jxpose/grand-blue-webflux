package com.grandblue.webflux.models.db;


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

  @Column("delete_flag")
  private Boolean deleteFlag;

  public ProductModel() {
  }

  public ProductModel(String productName, String productDescription) {
    this.productId = UUID.randomUUID();
    this.productName = productName;
    this.productDescription = productDescription;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public void setDeleteFlag(Boolean deleteFlag) {
    this.deleteFlag = deleteFlag;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

  public UUID getProductId() {
    return productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public Boolean getDeleteFlag() {
    return deleteFlag;
  }
}
