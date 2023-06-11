package com.grandblue.webflux.models.response;

import com.grandblue.webflux.models.db.ProductModel;

public record ProductResponse (String productId, String productName, String productDescription)
    implements GrandBlueResponse.ResponseData{

  public ProductResponse(ProductModel productModel) {
    this(productModel.getProductId().toString(), productModel.getProductName(), productModel.getProductDescription());
  }
}
