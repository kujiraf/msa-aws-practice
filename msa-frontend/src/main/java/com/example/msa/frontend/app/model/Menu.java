package com.example.msa.frontend.app.model;

public enum Menu {
  LOGOUT("ログアウト", "/logout"),
  PORTAL("ポータル", "/portal"),
  USER_MANAGEMENT("ユーザ管理", "/user-management");

  private String name;
  private String path;

  private Menu(String name, String path) {
    this.name = name;
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }
}
