package com.example.msa.common.apinfra.cloud.aws.log.dynamodb.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model.Log;
import com.example.msa.common.apinfra.cloud.aws.log.dynamodb.model.LogKey;

@EnableScan
public interface LogRepository extends CrudRepository<Log, LogKey> {}
