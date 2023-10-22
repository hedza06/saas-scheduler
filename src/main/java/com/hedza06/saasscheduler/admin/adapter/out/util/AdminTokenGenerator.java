package com.hedza06.saasscheduler.admin.adapter.out.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminTokenGenerator {
  public static String generate() {
    byte[] tokenBytes = new byte[64];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(tokenBytes);

    return Base64.getEncoder().encodeToString(tokenBytes);
  }
}
