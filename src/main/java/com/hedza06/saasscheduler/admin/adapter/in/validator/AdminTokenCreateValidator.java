package com.hedza06.saasscheduler.admin.adapter.in.validator;

import com.hedza06.saasscheduler.admin.application.domain.AdminToken;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase.AdminTokenCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AdminTokenCreateValidator implements Validator {

  private final AdminTokenUseCase adminTokenUseCase;
  private final PasswordEncoder passwordEncoder;


  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(AdminTokenCreateCommand.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    var createTokenCommand = (AdminTokenCreateCommand) target;
    validateCredentials(createTokenCommand, errors);
  }

  private void validateCredentials(AdminTokenCreateCommand createTokenCommand, Errors errors) {
    var providedUsername = createTokenCommand.username();
    var providedPassword = createTokenCommand.password();

    AdminToken adminToken = adminTokenUseCase.findByUsernameWithAppDetails(providedUsername);
    if (adminToken != null
        && adminToken.getApp() != null
        && !passwordEncoder.matches(providedPassword, adminToken.getApp().getPassword())
    ) {
      errors.rejectValue(
          "username",
          "token.already-exists",
          "Token for provided username and password already exists " +
              "or password is invalid"
      );
    }
  }
}
