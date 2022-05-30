package com.example.msa.frontend.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;
import com.example.msa.frontend.domain.repository.UserResourceRepository;

@Service
public class OrchestrationServiceImpl implements OrchestrationService {

  @Autowired UserResourceRepository userResourceRepository;

  @Override
  public UserResource getUserResource(String loginId) throws BusinessException {
    return userResourceRepository.findByLoginId(loginId);
  }
}
