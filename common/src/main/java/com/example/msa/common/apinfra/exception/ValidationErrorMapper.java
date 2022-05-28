package com.example.msa.common.apinfra.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

public class ValidationErrorMapper {

  public static ValidationError map(FieldError fieldError) {
    return ValidationError.builder()
        .objectName(fieldError.getObjectName())
        .field(fieldError.getField())
        .defaultMessage(fieldError.getDefaultMessage())
        .build();
  }

  public static List<ValidationError> map(List<FieldError> fieldErrors) {
    return fieldErrors.stream().map(ValidationErrorMapper::map).collect(Collectors.toList());
  }
}
