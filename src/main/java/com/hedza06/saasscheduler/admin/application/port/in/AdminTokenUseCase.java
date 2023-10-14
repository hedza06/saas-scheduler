package com.hedza06.saasscheduler.admin.application.port.in;

import com.hedza06.saasscheduler.admin.application.domain.AdminToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface AdminTokenUseCase {
  String create(AdminTokenCreateCommand command);

  AdminToken findByUsernameWithAppDetails(String username);

  record AdminTokenCreateCommand(
      @NotNull(message = "Username is required")
      @NotEmpty(message = "Username could not be empty")
      @NotBlank(message = "Username is invalid")
      String username,

      @NotNull(message = "Password is required")
      @NotEmpty(message = "Password could not be empty")
      @NotBlank(message = "Password is invalid")
      String password
  ) {
  }
}
