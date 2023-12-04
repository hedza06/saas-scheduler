package com.hedza06.saasscheduler.auth.adapter.in;

import com.hedza06.saasscheduler.admin.adapter.out.util.AdminTokenManager;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase;
import com.hedza06.saasscheduler.auth.application.exceptions.AdminTokenNotFoundException;
import com.hedza06.saasscheduler.auth.application.port.in.AccessTokenProviderUseCase;
import com.hedza06.saasscheduler.auth.application.port.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthAdapter implements AuthUseCase {

  private final AppUseCase appUseCase;
  private final AccessTokenProviderUseCase accessTokenProviderUseCase;


  @Override
  public String getAccessToken(String adminToken)
      throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException,
      NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    var appName = appUseCase
        .findNameByAdminToken(AdminTokenManager.encryptToken(adminToken))
        .orElseThrow(AdminTokenNotFoundException::new);

    return accessTokenProviderUseCase.provide(appName);
  }
}
