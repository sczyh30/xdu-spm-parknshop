package io.spm.parknshop.apply.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Eric Zhao
 */
public final class ApplyProcessorRoles {

  private static final String SELLER_PREFIX = "SELLER_";

  public static final String SYSTEM_AUTO = "SYS_AUTO_WORKFLOW";

  public static boolean isSeller(String processorId) {
    return !StringUtils.isEmpty(processorId) && processorId.startsWith(SELLER_PREFIX);
  }

  private ApplyProcessorRoles() {}
}
