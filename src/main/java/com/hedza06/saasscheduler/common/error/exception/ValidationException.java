package com.hedza06.saasscheduler.common.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;

@Getter
@RequiredArgsConstructor
public class ValidationException extends Exception {

  private final transient Errors errors;

}
