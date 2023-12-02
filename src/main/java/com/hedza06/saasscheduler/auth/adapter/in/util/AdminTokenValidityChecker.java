package com.hedza06.saasscheduler.auth.adapter.in.util;

import com.hedza06.saasscheduler.admin.adapter.out.util.AdminTokenManager;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class AdminTokenValidityChecker {

  private final AdminTokenUseCase adminTokenUseCase;

  public boolean isValid(String adminToken) throws Exception {
    return isNotBlank(adminToken)
        && adminTokenUseCase.existsByToken(AdminTokenManager.encryptToken(adminToken));
  }
}
