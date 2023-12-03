package com.hedza06.saasscheduler.auth.application.port.in;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthUseCase {

  String getAccessToken(String adminToken)
      throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException,
      NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
