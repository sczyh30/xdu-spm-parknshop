package io.spm.parknshop.common.util;

import io.spm.parknshop.common.exception.ServiceException;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * Util class for creating service exception instances.
 *
 * @author Eric Zhao
 */
public final class ExceptionUtils {

  public static ServiceException invalidParam(String paramName) {
    return new ServiceException(BAD_REQUEST, String.format("Invalid parameter: %s", paramName));
  }

  public static ServiceException loginIncorrect() {
    return new ServiceException(USER_LOGIN_INCORRECT, "Incorrect username or password");
  }

  public static ServiceException invalidToken() {
    return new ServiceException(USER_INVALID_TOKEN, "Invalid principal");
  }

  public static ServiceException authNoPermission() {
    return new ServiceException(NO_AUTH, "No permission for this operation");
  }

  public static ServiceException idNotMatch() {
    return new ServiceException(ID_NOT_MATCH, "Entity id does not match");
  }

  private ExceptionUtils() {}
}
