package com.example.msa.backend.usersvc.domain.service;

import java.util.List;
import com.example.msa.backend.usersvc.domain.model.entity.Credential;
import com.example.msa.backend.usersvc.domain.model.entity.User;
import com.example.msa.common.apinfra.exception.BusinessException;

public interface SampleService {

  public List<User> getUsers();

  public User getUser(Long id) throws BusinessException;

  public User getUserByLoginId(String loginId) throws BusinessException;

  public List<Credential> addCredentials(List<Credential> credentials) throws BusinessException;
}
