package io.spm.parknshop.common.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;

/**
 * @author Eric Zhao
 */
public final class AuthCenter {

  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String encryptDefault(String str) {
    return encoder.encode(str);
  }

  public static boolean decryptMatches(String origin, String encrypted) {
    if (StringUtils.isEmpty(origin) || StringUtils.isEmpty(encrypted)) {
      return false;
    }
    return encoder.matches(origin, encrypted);
  }

  public static String md5(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }
    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    char[] charArray = str.toCharArray();
    byte[] byteArray = new byte[charArray.length];

    for (int i = 0; i < charArray.length; i++)
      byteArray[i] = (byte) charArray[i];
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuilder hexValue = new StringBuilder();
    for (byte md5Byte : md5Bytes) {
      int val = ((int) md5Byte) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();
  }

  private AuthCenter() {
  }
}
