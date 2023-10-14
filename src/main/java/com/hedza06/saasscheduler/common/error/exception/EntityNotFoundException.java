package com.hedza06.saasscheduler.common.error.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityNotFoundException extends Exception {
  @Override
  public String getMessage() {
    return "Entity not found!";
  }
}
