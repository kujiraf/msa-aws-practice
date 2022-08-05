package com.example.msa.backend.usersvc.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(
    basePackages = "com.example.msa.common.apinfra.cloud.aws.log.dynamodb.repository")
public class DynamoDBConfig {}
