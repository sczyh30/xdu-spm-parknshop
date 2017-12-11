package io.spm.parknshop.user.domain;

public class LoginVO {
  private String token;
  private String username;
  private Long id;

  public LoginVO() {
  }

  public LoginVO(String token, String username, Long id) {
    this.token = token;
    this.username = username;
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public LoginVO setToken(String token) {
    this.token = token;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public LoginVO setUsername(String username) {
    this.username = username;
    return this;
  }

  public Long getId() {
    return id;
  }

  public LoginVO setId(Long id) {
    this.id = id;
    return this;
  }
}
