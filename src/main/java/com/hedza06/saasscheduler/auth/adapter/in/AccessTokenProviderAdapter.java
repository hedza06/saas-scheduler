package com.hedza06.saasscheduler.auth.adapter.in;

import com.hedza06.saasscheduler.auth.application.port.in.AccessTokenProviderUseCase;
import com.hedza06.saasscheduler.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccessTokenProviderAdapter implements AccessTokenProviderUseCase {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public String provide(String subject) {
    return jwtTokenProvider.generateToken(subject);
  }
}
