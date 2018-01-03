package io.spm.parknshop.apply.domain;

import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public final class ApplyProcessorRoles {

  public static final String SELLER_PREFIX = "SELLER_";
  public static final String CUSTOMER_PREFIX = "USER_";
  public static final String ADMIN_PREFIX = "ADMIN_";

  private static final String SYSTEM_AUTO = "SYS_AUTO_WORKFLOW";

  public static boolean isSysAuto(String processorId) {
    return !StringUtils.isEmpty(processorId) && SYSTEM_AUTO.equals(processorId);
  }

  public static boolean isCustomer(String processorId) {
    return !StringUtils.isEmpty(processorId) && processorId.startsWith(CUSTOMER_PREFIX);
  }

  public static boolean isSeller(String processorId) {
    return !StringUtils.isEmpty(processorId) && processorId.startsWith(SELLER_PREFIX);
  }

  public static boolean isAdmin(String processorId) {
    return !StringUtils.isEmpty(processorId) && processorId.startsWith(ADMIN_PREFIX);
  }

  public static Mono<Long> checkSellerId(String processorId) {
    if (StringUtils.isEmpty(processorId) || !processorId.startsWith(SELLER_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Invalid role"));
    }
    return Mono.just(processorId.replace(SELLER_PREFIX, ""))
      .map(Long::valueOf);
  }

  public static Tuple2<Integer, Long> getDetailed(/*@Normal*/ String processorId) {
    if (isAdmin(processorId)) {
      return Tuple2.of(AuthRoles.ADMIN, Long.valueOf(processorId.replace(ADMIN_PREFIX, "")));
    }
    if (isSysAuto(processorId)) {
      return Tuple2.of(AuthRoles.SYS_AUTO, 0L);
    }
    if (isSeller(processorId)) {
      return Tuple2.of(AuthRoles.SELLER, Long.valueOf(processorId.replace(SELLER_PREFIX, "")));
    }
    if (isCustomer(processorId)) {
      return Tuple2.of(AuthRoles.CUSTOMER, Long.valueOf(processorId.replace(CUSTOMER_PREFIX, "")));
    }
    return Tuple2.of(AuthRoles.UNKNOWN_ROLE, -1L);
  }

  public static Mono<Long> checkAdminId(String processorId) {
    if (StringUtils.isEmpty(processorId) || !processorId.startsWith(ADMIN_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Invalid role"));
    }
    return Mono.just(processorId.replace(ADMIN_PREFIX, ""))
      .map(Long::valueOf);
  }

  private ApplyProcessorRoles() {}
}
