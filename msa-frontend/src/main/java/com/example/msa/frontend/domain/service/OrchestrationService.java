package com.example.msa.frontend.domain.service;

import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;

public interface OrchestrationService {
  public UserResource getUserResource(String loginId) throws BusinessException;
}
