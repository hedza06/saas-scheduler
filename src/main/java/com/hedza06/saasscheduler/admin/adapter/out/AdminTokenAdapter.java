package com.hedza06.saasscheduler.admin.adapter.out;

import com.hedza06.saasscheduler.admin.adapter.out.persistence.AdminTokenRepository;
import com.hedza06.saasscheduler.admin.adapter.out.util.AdminTokenGenerator;
import com.hedza06.saasscheduler.admin.adapter.out.util.AdminTokenManager;
import com.hedza06.saasscheduler.admin.application.domain.AdminToken;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AdminTokenAdapter implements AdminTokenUseCase {

  private final AdminTokenRepository adminTokenRepository;


  @Override
  public String create(AdminTokenCreateCommand command)
      throws InvalidKeySpecException, NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    var adminToken = new AdminToken();
    var generatedToken = AdminTokenGenerator.generate();
    adminToken.setToken(AdminTokenManager.encryptToken(generatedToken));

    adminTokenRepository.save(adminToken);
    return adminToken.getToken();
  }

  @Override
  @Transactional(readOnly = true)
  public AdminToken findByUsernameWithAppDetails(String username) {
    return adminTokenRepository.findByUsernameWithApp(username);
  }
}
