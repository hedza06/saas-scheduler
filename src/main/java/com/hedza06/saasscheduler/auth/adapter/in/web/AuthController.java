package com.hedza06.saasscheduler.auth.adapter.in.web;

import com.hedza06.saasscheduler.auth.application.port.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthUseCase authUseCase;


  @SneakyThrows
  @GetMapping("access-token")
  @PreAuthorize("@adminTokenValidityChecker.isValid(#adminToken)")
  ResponseEntity<AccessTokenResponse> getTemporaryAccessToken(
      @RequestHeader(value = "X-ADMIN-TOKEN") String adminToken
  ) {
    var accessToken = authUseCase.getAccessToken(adminToken);
    return new ResponseEntity<>(new AccessTokenResponse(accessToken), HttpStatus.OK);
  }

  private record AccessTokenResponse(String accessToken) {
  }
}
