package com.example.msa.backend.usersvc.app.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.msa.backend.usersvc.domain.model.UserResourceMapper;
import com.example.msa.backend.usersvc.domain.service.SampleService;
import com.example.msa.common.apinfra.exception.BusinessException;
import com.example.msa.common.model.UserResource;

@RestController
@RequestMapping("api/v1")
public class BackendUserController {

  @Autowired private SampleService sampleService;

  @GetMapping("/users")
  public List<UserResource> getUsers() {
    return UserResourceMapper.mapWithCredentials(sampleService.getUsers());
  }

  @GetMapping("/users/{id:[0-9]+}")
  public UserResource getUser(@PathVariable Long id) throws BusinessException {
    return UserResourceMapper.mapWithCredentials(sampleService.getUser(id));
  }

  @GetMapping("/users/user")
  public UserResource getUserByLoginId(@RequestParam("loginId") String loginId)
      throws BusinessException {
    return UserResourceMapper.mapWithCredentials(sampleService.getUserByLoginId(loginId));
  }

  //  TODO
  //  @GetMapping("/users/{id:[0-9]+}/credentials")
  //  public List<CredentialResource> addTokens(
  //      @RequestBody List<CredentialResource> credentialResources) throws BusinessException {
  //    return CredentialResourceMapper.map(
  //
  // sampleService.addCredentials(CredentialResourceMapper.mapToEntity(credentialResources)));
  //  }
}
