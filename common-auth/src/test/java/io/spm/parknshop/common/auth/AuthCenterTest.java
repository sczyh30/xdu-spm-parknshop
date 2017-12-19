package io.spm.parknshop.common.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCenterTest {

  @Test
  void decryptMatches() {
    final String password = "a6cd1^x83S3x(";
    String encrypted = AuthCenter.encryptDefault(password);
    assertTrue(AuthCenter.decryptMatches(password, encrypted));
  }
}