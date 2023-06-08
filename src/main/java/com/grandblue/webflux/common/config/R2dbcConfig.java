package com.grandblue.webflux.common.config;

import com.grandblue.webflux.common.converter.UUIDConverter;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.List;

@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {


  @Override
  public ConnectionFactory connectionFactory() {
    MySqlConnectionConfiguration connectionConfiguration = MySqlConnectionConfiguration.builder()
//        .host("localhost")
//        .user("USER_NAME")
//        .password("PASSWORD")
//        .port(3306)
//        .database("grand_blue")
//        .sslMode(SslMode.DISABLED)
//        .serverZoneId(ZoneId.systemDefault())
        .build();

    return MySqlConnectionFactory.from(connectionConfiguration);
  }

  @Override
  public R2dbcCustomConversions r2dbcCustomConversions() {
    return new R2dbcCustomConversions(getStoreConversions(),
        List.of(
            new UUIDConverter.UUIDToByteBufferConverter(),
            new UUIDConverter.ByteBufferToUUIDConverter()
        )
    );
  }
}

