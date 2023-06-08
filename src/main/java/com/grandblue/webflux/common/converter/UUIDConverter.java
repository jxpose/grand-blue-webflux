package com.grandblue.webflux.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class UUIDConverter {

  @WritingConverter
  public static class UUIDToByteBufferConverter implements Converter<UUID, ByteBuffer> {
    @Override
    public ByteBuffer convert(UUID source) {
      return ByteBuffer.wrap(new byte[16])
          .putLong(source.getMostSignificantBits())
          .putLong(source.getLeastSignificantBits())
          .rewind();
    }
  }

  @ReadingConverter
  public static class ByteBufferToUUIDConverter implements Converter<ByteBuffer, UUID> {
    @Override
    public UUID convert(ByteBuffer source) {
      long mostSignificantBits = source.getLong();
      long leastSignificantBits = source.getLong();
      return new UUID(mostSignificantBits, leastSignificantBits);
    }
  }
}
