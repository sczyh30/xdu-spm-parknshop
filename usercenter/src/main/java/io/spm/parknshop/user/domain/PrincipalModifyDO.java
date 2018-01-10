package io.spm.parknshop.user.domain;

/**
 * @author Eric Zhao
 */
public class PrincipalModifyDO {

  private Long id;

  private String oldPassword;
  private String newPassword;

  public Long getId() {
    return id;
  }

  public PrincipalModifyDO setId(Long id) {
    this.id = id;
    return this;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public PrincipalModifyDO setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
    return this;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public PrincipalModifyDO setNewPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  @Override
  public String toString() {
    return "PrincipalModifyDO{" +
      "id=" + id +
      ", oldPassword='" + oldPassword + '\'' +
      ", newPassword='" + newPassword + '\'' +
      '}';
  }
}
