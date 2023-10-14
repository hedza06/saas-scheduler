package com.hedza06.saasscheduler.auth.application.port.in;

public interface AuthUseCase {

  String getAccessToken(String adminToken);
}
