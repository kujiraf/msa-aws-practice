package com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model;

import java.io.Serializable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LogKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @DynamoDBHashKey private String userId;
  @DynamoDBHashKey private String createdAt;
}
