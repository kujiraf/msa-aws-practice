package com.example.msa.backend.usersvc.domain.model.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CredentialPK implements Serializable {

  private static final long serialVersionUID = 1L;

  private long userId;
  private String credentialType;

  @Column(name = "user_id", nullable = false)
  @Id
  public long getUserId() {
    return userId;
  }

  @Column(name = "credential_type", nullable = false)
  @Id
  public String getCredentialType() {
    return credentialType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CredentialPK that = (CredentialPK) o;
    return userId == that.userId && Objects.equals(credentialType, that.credentialType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, credentialType);
  }
}
