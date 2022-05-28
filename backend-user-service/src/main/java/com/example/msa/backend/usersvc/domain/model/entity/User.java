package com.example.msa.backend.usersvc.domain.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usr", schema = "public", catalog = "sample")
@Setter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private long userId;
  private String firstName;
  private String familyName;
  private String loginId;
  private Boolean isLogin;
  private Boolean isAdmin;
  private Integer ver;
  private Timestamp lastUpdatedAt;
  private Set<Credential> credentialsByUserId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return userId == user.userId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, firstName, familyName, loginId, isLogin, ver, lastUpdatedAt);
  }

  @Id
  @Column(name = "user_id", nullable = false)
  public long getUserId() {
    return userId;
  }

  @Basic
  @Column(name = "first_name", nullable = true, length = 512)
  public String getFirstName() {
    return firstName;
  }

  @Basic
  @Column(name = "family_name", nullable = true, length = 512)
  public String getFamilyName() {
    return familyName;
  }

  @Basic
  @Column(name = "login_id", nullable = true, length = 32)
  public String getLoginId() {
    return loginId;
  }

  @Basic
  @Column(name = "is_login", nullable = true)
  public Boolean getIsLogin() {
    return isLogin;
  }

  @Basic
  @Column(name = "is_admin", nullable = true)
  public Boolean getIsAdmin() {
    return isAdmin;
  }

  @Basic
  @Column(name = "ver", nullable = true)
  public Integer getVer() {
    return ver;
  }

  @Basic
  @Column(name = "last_updated_at", nullable = true)
  public Timestamp getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  @OneToMany(
      mappedBy = "usrByUserId",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  public Set<Credential> getCredentialsByUserId() {
    return credentialsByUserId;
  }
}
