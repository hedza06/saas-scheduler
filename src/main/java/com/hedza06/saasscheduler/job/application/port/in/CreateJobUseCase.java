package com.hedza06.saasscheduler.job.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.quartz.SchedulerException;

import java.util.HashMap;

public interface CreateJobUseCase {

  String create(CreateJobCommand createCommand) throws SchedulerException;

  record CreateJobCommand(
      @NotNull(message = "Definition is missing")
      @NotEmpty(message = "Definition is empty")
      @NotBlank(message = "Invalid definition")
      String definition,

      @NotNull(message = "Description is missing")
      @NotEmpty(message = "Description is empty")
      @NotBlank(message = "Invalid description")
      String description,

      @NotNull(message = "URL is missing")
      @NotEmpty(message = "URL is empty")
      @NotBlank(message = "Invalid URL")
      String url,

      HashMap<String, String> requestHeaders,

      HashMap<String, Object> requestBody
  ) {
  }
}
