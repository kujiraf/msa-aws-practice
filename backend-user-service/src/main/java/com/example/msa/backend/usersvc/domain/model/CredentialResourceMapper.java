package com.example.msa.backend.usersvc.domain.model;

import java.util.List;
import java.util.stream.Collectors;
import com.example.msa.backend.usersvc.domain.model.entity.Credential;
import com.example.msa.common.model.CredentialResource;

public class CredentialResourceMapper {

  /**
   * CredentialをCredentialResourceにマッピングする
   *
   * @param credential
   * @return マッピングされたCredentialResource
   */
  public static CredentialResource map(Credential credential) {
    return CredentialResource.builder()
        .userId(credential.getUserId())
        .credentialType(credential.getCredentialType())
        .credentialKey(credential.getCredentialKey())
        .validDate(credential.getValidDate())
        .build();
  }

  /**
   * List<Credential>をList<CredentialResource>にマッピングする
   *
   * @param credentials
   * @return マッピングされたList<CredentialResource>
   */
  public static List<CredentialResource> map(List<Credential> credentials) {
    return credentials.stream().map(CredentialResourceMapper::map).collect(Collectors.toList());
  }

  /**
   * CredentialResourceをCredentialにマッピングする
   *
   * @param credentialResource
   * @return マッピングされたCredential
   */
  public static Credential mapToEntity(CredentialResource credentialResource) {
    return Credential.builder()
        .userId(credentialResource.getUserId())
        .credentialType(credentialResource.getCredentialType())
        .credentialKey(credentialResource.getCredentialKey())
        .validDate(credentialResource.getValidDate())
        .build();
  }

  /**
   * List<CredentialResource>をList<Credential>にマッピングする
   *
   * @param credentialResource
   * @return マッピングされたList<Credential>
   */
  public static List<Credential> mapToEntity(List<CredentialResource> resources) {
    return resources.stream()
        .map(CredentialResourceMapper::mapToEntity)
        .collect(Collectors.toList());
  }
}
