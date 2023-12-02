package com.hedza06.saasscheduler.admin.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hedza06.saasscheduler.admin.adapter.in.validator.AdminTokenCreateValidator;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase;
import com.hedza06.saasscheduler.admin.application.port.in.AdminTokenUseCase.AdminTokenCreateCommand;
import com.hedza06.saasscheduler.common.error.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminTokenController {

  private final AdminTokenCreateValidator createTokenValidator;
  private final AdminTokenUseCase adminTokenUseCase;


  @PostMapping("token")
  @SneakyThrows(ValidationException.class)
  ResponseEntity<AdminTokenResponse> createAdminToken(
      @RequestBody @Valid AdminTokenCreateCommand command
  ) throws Exception {
    Errors potentialErrors = new BeanPropertyBindingResult(command, "command");
    ValidationUtils.invokeValidator(createTokenValidator, command, potentialErrors);

    if (potentialErrors.hasErrors()) {
      throw new ValidationException(potentialErrors);
    }

    String adminToken = adminTokenUseCase.create(command);
    return new ResponseEntity<>(new AdminTokenResponse(adminToken), HttpStatus.CREATED);
  }

  record AdminTokenResponse(@JsonProperty(value = "adminToken") String adminToken) {
  }
}
