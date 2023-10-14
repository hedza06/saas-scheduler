package com.hedza06.saasscheduler.admin.adapter.in.web;

import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase.AppCreateCommand;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class AppController {

  private final AppUseCase appUseCase;


  @PostMapping
  ResponseEntity<Void> create(@RequestBody AppCreateCommand command) {
    appUseCase.create(command);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}/activate")
  @SneakyThrows(EntityNotFoundException.class)
  ResponseEntity<Void> activate(@PathVariable Integer id) {
    appUseCase.activate(id);
    return ResponseEntity.noContent().build();
  }
}
