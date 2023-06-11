package com.grandblue.webflux.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GrandBlueResponse(List<ResponseData> responseData, String[] errorMessages) {

  public static interface ResponseData {
  }
}
