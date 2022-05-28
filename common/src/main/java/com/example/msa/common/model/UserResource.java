package com.example.msa.common.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResource implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userId;
  private String firstName;
  private String familyName;
  private String loginId;
  private Boolean isLogin;
  private Boolean isAdmin;
  private List<CredentialResource> credentialResources;
}
