package io.spm.parknshop.common.auth;

/**
 * Authentication roles.
 *
 * @author Eric Zhao
 */
public enum AuthRole {
  CUSTOMER(0), SELLER(1), ADMIN(11);

  private int role;

  AuthRole(int role) {
    this.role = role;
  }

  public int getRole() {
    return role;
  }
}
