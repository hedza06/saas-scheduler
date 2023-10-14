package com.hedza06.saasscheduler.auth.adapter.in;

import com.hedza06.saasscheduler.auth.application.port.in.AccessTokenProviderUseCase;
import com.hedza06.saasscheduler.auth.application.port.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthAdapter implements AuthUseCase {

  private final AccessTokenProviderUseCase accessTokenProviderUseCase;


  @Override
  public String getAccessToken(String adminToken) {
    // TODO: generate temporary access token...
    // TODO: use token provider to get symmetrically encrypted access token
    return null;
  }
}
