package com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "") // TODO DynamoDBのテーブル名を追加する
public class Log implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private LogKey logKey;

  private String userId;
  private String createdAt;

  @DynamoDBAttribute private String traceId;

  @DynamoDBHashKey
  public String getUserId() {
    return userId;
  }

  @DynamoDBHashKey
  public String getCreatedAt() {
    return createdAt;
  }
}
