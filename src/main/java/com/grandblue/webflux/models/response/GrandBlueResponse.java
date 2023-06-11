package com.grandblue.webflux.models.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record GrandBlueResponse<T extends GrandBlueResponse.ResponseData>(List<T> responseData,
                                                                          List<String> errorMessages) {

  public GrandBlueResponse(T responseData) {
    this(Collections.singletonList(responseData), null);
  }

  public GrandBlueResponse(List<T> responseData) {
    this(responseData, null);
  }

  public static <T extends ResponseData> GrandBlueResponse<T> error(List<String> errorMessages) {
    return new GrandBlueResponse<>(null, errorMessages);
  }

  public interface ResponseData {
  }
}
