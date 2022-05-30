package com.example.msa.backend.usersvc.domain.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.msa.backend.usersvc.domain.model.entity.Credential;
import com.example.msa.backend.usersvc.domain.model.entity.User;
import com.example.msa.backend.usersvc.domain.repository.jpa.UserRepository;
import com.example.msa.common.apinfra.exception.BusinessException;

@Service
public class SampleServiceImpl implements SampleService {

  @Autowired UserRepository userRepository;
  @Autowired MessageSource messageSource;

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUser(Long id) throws BusinessException {
    User user = userRepository.findByUserId(id);
    throwExceptionIfNotFindUser(user, "BE0001", id.toString());
    return user;
  }

  @Override
  public User getUserByLoginId(String loginId) throws BusinessException {
    User user = userRepository.findByLoginId(loginId);
    throwExceptionIfNotFindUser(user, "BE0002", loginId);
    return user;
  }

  @Transactional
  @Override
  public List<Credential> addCredentials(List<Credential> credentials) throws BusinessException {
    Integer updateTargetUserId = 0;
    credentials =
        credentials.stream()
            .map(
                c -> {
                  c.setUserId(updateTargetUserId);
                  return c;
                })
            .collect(Collectors.toList());
    User user = userRepository.findByUserId(updateTargetUserId);
    throwExceptionIfNotFindUser(user, "BE0001", updateTargetUserId.toString());
    user.getCredentialsByUserId().clear();
    userRepository.flush();
    user.getCredentialsByUserId().addAll(credentials.stream().collect(Collectors.toSet()));
    return credentials;
  }

  private void throwExceptionIfNotFindUser(User user, String errorCode, String id)
      throws BusinessException {
    if (Objects.isNull(user)) {
      throw new BusinessException(
          errorCode, messageSource.getMessage(errorCode, new String[] {id}, Locale.getDefault()));
    }
  }
}
