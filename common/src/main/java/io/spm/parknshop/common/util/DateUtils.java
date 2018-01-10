package io.spm.parknshop.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Util class for date and time.
 *
 * @author Eric Zhao
 * @date 2017/12/1
 */
public final class DateUtils {

  public static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static LocalDate toLocalDate(Date date) {
    return toLocalDateTime(date).toLocalDate();
  }

  public static Date toDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date toDate(LocalDate date) {
    return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  private DateUtils() {
  }
}
