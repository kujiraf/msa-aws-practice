package com.example.msa.common.apinfra.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
// @JsonTypeName("ValidationErrorResponse")
public class ValidationErrorResponse implements ErrorResponse {
  private List<ValidationError> validationErrors;
}
