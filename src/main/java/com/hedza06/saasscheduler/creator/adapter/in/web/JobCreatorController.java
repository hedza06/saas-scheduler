package com.hedza06.saasscheduler.creator.adapter.in.web;

import com.hedza06.saasscheduler.creator.application.port.in.JobCreatorUseCase;
import com.hedza06.saasscheduler.creator.application.port.in.JobCreatorUseCase.JobCreateCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-creator")
public class JobCreatorController {

  private final JobCreatorUseCase jobCreatorUseCase;


  @PostMapping("create")
  @SneakyThrows(SchedulerException.class)
  ResponseEntity<JobCreateResponse> create(@Valid @RequestBody JobCreateCommand command) {
    var jobIdentity = jobCreatorUseCase.create(command);
    return new ResponseEntity<>(
        new JobCreateResponse(jobIdentity),
        HttpStatus.CREATED
    );
  }

  private record JobCreateResponse(String jobId) {
  }
}
