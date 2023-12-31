package com.hedza06.saasscheduler.admin.adapter.out;

import com.hedza06.saasscheduler.admin.adapter.out.persistence.AppRepository;
import com.hedza06.saasscheduler.admin.application.domain.App;
import com.hedza06.saasscheduler.admin.application.port.in.AppUseCase;
import com.hedza06.saasscheduler.common.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

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

  @Override
  public App getByUsername(String username) {
    var id = appRepository.findIdByUsername(username);
    return appRepository.getReferenceById(id);
  }

  @Override
  public Optional<String> findNameByAdminToken(String token) {
    var appNames = appRepository.findNameByAdminToken(token);
    return isNotEmpty(appNames) ? Optional.of(appNames.getFirst()) : Optional.empty();
  }
}
