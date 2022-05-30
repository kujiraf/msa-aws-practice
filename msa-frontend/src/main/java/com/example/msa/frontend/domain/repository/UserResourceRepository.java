package com.example.msa.frontend.domain.repository;

import java.util.List;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;

public interface UserResourceRepository {

  public UserResource findOne(String userId);

  public UserResource findByLoginId(String loginId) throws BusinessException;

  public List<UserResource> findAll();
}
