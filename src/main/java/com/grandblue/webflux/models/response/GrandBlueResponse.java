package com.grandblue.webflux.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GrandBlueResponse(ResponseData responseData, String[] errorMessages) {

  public static interface ResponseData {
  }
}
