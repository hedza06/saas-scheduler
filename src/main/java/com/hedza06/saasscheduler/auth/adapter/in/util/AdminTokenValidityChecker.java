package com.hedza06.saasscheduler.auth.adapter.in.util;

import com.hedza06.saasscheduler.admin.adapter.out.persistence.AdminTokenRepository;
import com.hedza06.saasscheduler.admin.adapter.out.util.AdminTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class AdminTokenValidityChecker {

  private final AdminTokenRepository adminTokenRepository;

  public boolean isValid(String adminToken) throws Exception {
    return isNotBlank(adminToken)
        && adminTokenRepository.existsByToken(AdminTokenManager.encryptToken(adminToken));
  }
}
