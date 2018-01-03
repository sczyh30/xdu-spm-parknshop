package io.spm.parknshop.common.auth;

/**
 * @author Eric Zhao
 */
public final class AuthRoles {

  public static final int UNKNOWN_ROLE = -1;
  public static final int CUSTOMER = 0;
  public static final int SELLER = 1;
  public static final int ADMIN = 11;
  public static final int SYS_AUTO = 12;

  private AuthRoles() {}
}
