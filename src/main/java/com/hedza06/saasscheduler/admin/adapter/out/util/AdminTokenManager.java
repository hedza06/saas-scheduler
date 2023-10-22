package com.hedza06.saasscheduler.admin.adapter.out.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdminTokenManager {
  private static final String SECRET_KEY_ALGORITHM = "AES";
  private static final String ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5Padding";
  private static final String SECRET_KEY = "dimpO442gzR3RppPzDkVTMr6HaEXSfhZ"; // TODO: store somewhere else...
  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 256;


  public static String encryptToken(String token) throws Exception {
    SecretKey secretKey = generateSecretKey();
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(token.getBytes());

    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  public static String decryptToken(String encryptedToken) throws Exception {
    SecretKey secretKey = generateSecretKey();
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedToken));
    return new String(decryptedBytes);
  }

  private static SecretKey generateSecretKey() throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(
        SECRET_KEY.toCharArray(), SECRET_KEY.getBytes(), ITERATION_COUNT, KEY_LENGTH
    );
    SecretKey tmp = factory.generateSecret(spec);
    return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
  }
}
