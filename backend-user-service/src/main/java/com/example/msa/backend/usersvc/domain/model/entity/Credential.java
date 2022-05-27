package com.example.msa.backend.usersvc.domain.model.entity;

import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Entity
@IdClass(CredentialPK.class)
public class Credential {

  private long userId;
  private String credentialType;
  private String credentialKey;
  private Timestamp validDate;
  private Integer ver;
  private User usrByUserId;

  @Id
  @Column(name = "user_id", nullable = false)
  public long getUserId() {
    return userId;
  }

  @Id
  @Column(name = "credential_type", nullable = false)
  public String getCredentialType() {
    return credentialType;
  }

  @Basic
  @Column(name = "credential_key", nullable = true, length = 2048)
  public String getCredentialKey() {
    return credentialKey;
  }

  @Basic
  @Column(name = "valid_date", nullable = true)
  public Timestamp getValidDate() {
    return validDate;
  }

  @Basic
  @Column(name = "ver", nullable = true)
  public Integer getVer() {
    return ver;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "user_id",
      referencedColumnName = "user_id",
      nullable = false,
      insertable = false,
      updatable = false)
  @OrderColumn
  public User getUsrByUserId() {
    return usrByUserId;
  }
}
