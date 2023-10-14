package com.hedza06.saasscheduler.admin.adapter.out;

import com.hedza06.saasscheduler.admin.adapter.out.persistence.AppRepository;
import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AppAdapter implements AppUseCase {

  private final AppRepository appRepository;


  @Override
  public void create(AppCreateCommand command) {
    // TODO: logic goes here...
  }

  @Override
  public void activate(Integer id) throws EntityNotFoundException {
    var app = appRepository
        .findById(id)
        .orElseThrow(EntityNotFoundException::new);

    app.setIsActive(true);
  }
}
