package com.hedza06.saasscheduler.admin.application.port.in;

import com.hedza06.saasscheduler.admin.application.domain.AdminToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AdminTokenUseCase {
  String create(AdminTokenCreateCommand command)
      throws InvalidKeySpecException, NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException;

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
