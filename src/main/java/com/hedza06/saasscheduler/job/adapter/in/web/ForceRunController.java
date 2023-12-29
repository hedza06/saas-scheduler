package com.hedza06.saasscheduler.job.adapter.in.web;

import com.hedza06.saasscheduler.job.application.port.out.ForceRunJobUseCase;
import com.hedza06.saasscheduler.job.application.port.out.ForceRunJobUseCase.ForceRunCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job/http/force-run")
public class ForceRunController {

  private final ForceRunJobUseCase forceRunJobUseCase;

  @PutMapping("{id}")
  @SneakyThrows(SchedulerException.class)
  ResponseEntity<Map<String, String>> forceRunHttpJob(
      @PathVariable String id,
      @RequestBody @Valid ForceRunCommand command
  ) {
    var jobKey = forceRunJobUseCase.httpForceRun(id, command);
    return new ResponseEntity<>(singletonMap("jobKey", jobKey), HttpStatus.OK);
  }

}
