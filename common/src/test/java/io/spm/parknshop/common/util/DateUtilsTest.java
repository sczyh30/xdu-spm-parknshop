package io.spm.parknshop.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for {@link DateUtils} class.
 *
 * @author Eric Zhao
 */
class DateUtilsTest {

  @SuppressWarnings("deprecation")
  @Test
  void toLocalDateTime() {
    Date date = new Date();
    int h = 1, m = 24;
    date.setHours(h);
    date.setMinutes(m);
    LocalDateTime dateTime = DateUtils.toLocalDateTime(date);
    assertEquals(dateTime.getHour(), h);
    assertEquals(dateTime.getMinute(), m);
  }
}