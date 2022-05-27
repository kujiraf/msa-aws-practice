package com.example.msa.backend.usersvc.domain.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.msa.backend.usersvc.domain.model.entity.Credential;
import com.example.msa.backend.usersvc.domain.model.entity.User;
import com.example.msa.common.apinfra.exception.BusinessException;

@Service
public class SampleServiceImpl implements SampleService {

  @Override
  public List<User> getUsers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User getUser(Long id) throws BusinessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User getUserByLoginId(String loginId) throws BusinessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Credential> addCredentials(List<Credential> credentials) throws BusinessException {
    // TODO Auto-generated method stub
    return null;
  }
}
