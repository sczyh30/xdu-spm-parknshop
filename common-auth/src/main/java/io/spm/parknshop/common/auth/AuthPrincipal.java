package io.spm.parknshop.common.auth;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class AuthPrincipal {

  private String id;
  private String username;
  private int role;
  private Date expireDate;

  public String getId() {
    return id;
  }

  public AuthPrincipal setId(String id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public AuthPrincipal setUsername(String username) {
    this.username = username;
    return this;
  }

  public int getRole() {
    return role;
  }

  public AuthPrincipal setRole(int role) {
    this.role = role;
    return this;
  }

  public Date getExpireDate() {
    return expireDate;
  }

  public AuthPrincipal setExpireDate(Date expireDate) {
    this.expireDate = expireDate;
    return this;
  }

  @Override
  public String toString() {
    return "AuthPrincipal{" +
      "id='" + id + '\'' +
      ", username='" + username + '\'' +
      ", role=" + role +
      ", expireDate=" + expireDate +
      '}';
  }
}
