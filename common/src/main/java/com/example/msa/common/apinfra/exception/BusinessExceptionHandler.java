package com.example.msa.common.apinfra.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    e.setStackTrace(new StackTraceElement[] {});
    return new ResponseEntity<ErrorResponse>(
        BusinessExceptionResponse.builder().businessException(e).build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
    BindingResult bindingResult = null;
    if (e instanceof MethodArgumentNotValidException) {
      bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
    } else if (e instanceof BindException) {
      bindingResult = ((BindException) e).getBindingResult();
    }
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    return new ResponseEntity<>(
        ValidationErrorResponse.builder()
            .validationErrors(ValidationErrorMapper.map(fieldErrors))
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
