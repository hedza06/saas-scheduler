package com.hedza06.saasscheduler.job.application.port.out;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.quartz.SchedulerException;

import java.util.HashMap;

public interface ForceRunJobUseCase {

  String httpForceRun(String id, ForceRunCommand command) throws SchedulerException;

  record ForceRunCommand(
      @NotNull(message = "URL is missing")
      @NotEmpty(message = "URL is empty")
      @NotBlank(message = "Invalid URL")
      String url,

      HashMap<String, String> requestHeaders,

      HashMap<String, Object> requestBody
  ) {
  }
}
