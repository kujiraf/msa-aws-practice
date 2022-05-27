package com.example.msa.common.model;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CredentialResource implements Serializable {

  private static final long serialVersionUID = 1L;

  private long userId;
  private String credentialType;
  private String credentialKey;
  private Timestamp validDate;
}
