package com.hedza06.saasscheduler.auth.application.exceptions;


public class AdminTokenNotFoundException extends RuntimeException {

  public AdminTokenNotFoundException() {
    super();
  }

  @Override
  public String getMessage() {
    return "Admin token not found!";
  }
}
