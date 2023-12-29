package com.hedza06.saasscheduler.job.adapter.in.web;

import com.hedza06.saasscheduler.job.application.port.in.CreateJobUseCase;
import com.hedza06.saasscheduler.job.application.port.in.CreateJobUseCase.CreateJobCommand;
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
@RequestMapping("/api/job/create")
public class CreateController {

  private final CreateJobUseCase createJobUseCase;


  @PostMapping
  @SneakyThrows(SchedulerException.class)
  ResponseEntity<JobCreateResponse> create(@Valid @RequestBody CreateJobCommand command) {
    var jobIdentity = createJobUseCase.create(command);
    return new ResponseEntity<>(
        new JobCreateResponse(jobIdentity),
        HttpStatus.CREATED
    );
  }

  private record JobCreateResponse(String jobId) {
  }
}
