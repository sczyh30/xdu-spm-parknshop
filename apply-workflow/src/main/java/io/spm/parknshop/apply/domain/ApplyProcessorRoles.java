package io.spm.parknshop.apply.domain;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
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

  public static Mono<Long> getSellerId(String processorId) {
    if (StringUtils.isEmpty(processorId) || !processorId.startsWith(SELLER_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Invalid role or id"));
    }
    return Mono.just(processorId.replace(SELLER_PREFIX, ""))
      .map(Long::valueOf);
  }

  private ApplyProcessorRoles() {}
}
