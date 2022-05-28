package com.example.msa.common.apinfra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends Exception {

  private static final long serialVersionUID = 1L;

  private String code;
  private Object[] args;

  public BusinessException(String code, String message) {
    super(code);
    this.code = code;
  }
}
