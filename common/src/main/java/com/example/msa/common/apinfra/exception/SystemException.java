package com.example.msa.common.apinfra.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String code;
  private Object[] args;

  public SystemException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }
}
