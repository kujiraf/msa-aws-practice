package com.example.msa.backend.usersvc.domain.model;

import java.util.List;
import java.util.stream.Collectors;
import com.example.msa.backend.usersvc.domain.model.entity.User;
import com.example.msa.common.model.UserResource;

public class UserResourceMapper {

  public static UserResource map(User user) {
    return UserResource.builder()
        .userId(Long.toString(user.getUserId()))
        .firstName(user.getFirstName())
        .familyName(user.getFamilyName())
        .loginId(user.getLoginId())
        .isLogin(user.getIsLogin())
        .isAdmin(user.getIsAdmin())
        .build();
  }

  public static UserResource mapWithCredentials(User user) {
    UserResource userResource = map(user);
    userResource.setCredentialResources(
        user.getCredentialsByUserId().stream()
            .map(CredentialResourceMapper::map)
            .collect(Collectors.toList()));
    return userResource;
  }

  public static List<UserResource> mapWithCredentials(List<User> users) {
    return users.stream().map(UserResourceMapper::mapWithCredentials).collect(Collectors.toList());
  }
}
