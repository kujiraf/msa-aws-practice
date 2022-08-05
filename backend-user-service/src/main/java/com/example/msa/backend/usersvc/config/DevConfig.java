package com.example.msa.backend.usersvc.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.xray.sql.TracingDataSource;

@Profile("dev")
@Configuration
public class DevConfig {

  @Bean
  DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.builder()
        .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix("dev_"))
        .build();
  }

  @Bean
  public DataSource dataSource() {
    return new TracingDataSource(
        new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:schema-hsql.sql")
            .addScript("classpath:data-hsql.sql")
            .build());
  }
}
