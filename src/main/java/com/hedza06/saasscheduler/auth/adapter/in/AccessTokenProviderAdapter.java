package com.hedza06.saasscheduler.auth.adapter.in;

import com.hedza06.saasscheduler.auth.application.port.in.AccessTokenProviderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccessTokenProviderAdapter implements AccessTokenProviderUseCase {
  @Override
  public String provide() {
    // TODO: logic for providing temporary (delegate) access token goes here...
    return null;
  }
}
