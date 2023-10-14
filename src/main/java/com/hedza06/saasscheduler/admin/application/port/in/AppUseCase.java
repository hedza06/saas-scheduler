package com.hedza06.saasscheduler.admin.application.port.in;

import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface AppUseCase {

  void create(AppCreateCommand command);

  void activate(Integer id) throws EntityNotFoundException;

  record AppCreateCommand(
      @NotNull(message = "Username is required")
      @NotBlank(message = "Username is invalid")
      String username,

      @NotNull(message = "Password is required")
      @NotBlank(message = "Password is invalid")
      String password,

      @NotNull(message = "App name is required")
      @NotBlank(message = "App name is invalid")
      String name
  ) {
  }
}
