package com.example.msa.common.apinfra.exception;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonTypeName("BusinessExceptionResponse")
public class BusinessExceptionResponse implements ErrorResponse {
  private BusinessException businessException;
}
