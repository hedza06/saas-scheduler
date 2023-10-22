package com.hedza06.saasscheduler.admin.adapter.out;

import com.hedza06.saasscheduler.admin.adapter.out.persistence.AppRepository;
import com.hedza06.saasscheduler.admin.application.domain.App;
import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AppAdapter implements AppUseCase {

  private final AppRepository appRepository;
  private final PasswordEncoder passwordEncoder;


  @Override
  public void create(AppCreateCommand command) {
    var app = new App();
    app.setUsername(command.username());
    app.setPassword(passwordEncoder.encode(command.password()));
    app.setName(command.name());
    app.setCreatedAt(LocalDateTime.now());

    appRepository.save(app);
  }

  @Override
  public void activate(Integer id) throws EntityNotFoundException {
    var app = appRepository
        .findById(id)
        .orElseThrow(EntityNotFoundException::new);

    app.setIsActive(true);
  }
}
