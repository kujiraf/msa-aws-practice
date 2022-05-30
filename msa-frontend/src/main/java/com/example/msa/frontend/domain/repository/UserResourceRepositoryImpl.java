package com.example.msa.frontend.domain.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.apinfra.exception.BusinessExceptionResponse;
import com.example.msa.common.apinfra.exception.ErrorResponse;
import com.example.msa.common.apinfra.exception.SystemException;
import com.example.msa.common.model.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;

// @Profile("v1") TODO
@Component
public class UserResourceRepositoryImpl implements UserResourceRepository {

  private static final String SERVICE_NAME = "/backend/user";
  private static final String API_VERSION = "/api/v1";

  @Autowired WebClient webClient;
  @Autowired ObjectMapper objectMapper;
  @Autowired MessageSource messageSource;

  @Override
  public UserResource findOne(String userId) {
    String endpoint = SERVICE_NAME + API_VERSION + "/user/{userId}";
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path(endpoint).build(userId))
        .retrieve()
        .bodyToMono(UserResource.class)
        .block();
  }

  @Override
  public UserResource findByLoginId(String loginId) throws BusinessException {
    String endpoint = SERVICE_NAME + API_VERSION + "/users/user";
    try {
      return webClient
          .get()
          .uri(builder -> builder.path(endpoint).queryParam("loginId", loginId).build())
          .retrieve()
          .bodyToMono(UserResource.class)
          .block();
    } catch (WebClientResponseException e) {
      try {
        ErrorResponse errorResponse =
            objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        if (errorResponse instanceof BusinessExceptionResponse) {
          throw ((BusinessExceptionResponse) errorResponse).getBusinessException();
        } else {
          String errorCode = "SE0002";
          throw new SystemException(
              errorCode,
              messageSource.getMessage(errorCode, new String[] {endpoint}, Locale.getDefault()),
              e);
        }
      } catch (IOException e1) {
        String errorCode = "SE0002";
        throw new SystemException(
            errorCode,
            messageSource.getMessage(errorCode, new String[] {endpoint}, Locale.getDefault()),
            e);
      }
    }
  }

  @Override
  public List<UserResource> findAll() {
    String endpoint = SERVICE_NAME + API_VERSION + "/users";
    return Arrays.asList(
        webClient
            .get()
            .uri(builder -> builder.path(endpoint).build())
            .retrieve()
            .bodyToMono(UserResource.class)
            .block());
  }
}
