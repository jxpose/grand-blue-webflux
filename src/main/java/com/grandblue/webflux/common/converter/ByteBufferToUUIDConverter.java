package com.grandblue.webflux.common.converter;


import org.springframework.core.convert.converter.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ByteBufferToUUIDConverter implements Converter<ByteBuffer, UUID> {

  @Override
  public UUID convert(ByteBuffer source) {
    if (source == null || source.remaining() != 16) {
      return null;
    }
    long mostSignificantBits = source.getLong();
    long leastSignificantBits = source.getLong();
    return new UUID(mostSignificantBits, leastSignificantBits);
  }
}
