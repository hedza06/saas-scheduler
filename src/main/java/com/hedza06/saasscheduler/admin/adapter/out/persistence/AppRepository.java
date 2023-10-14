package com.hedza06.saasscheduler.admin.adapter.out.persistence;

import com.hedza06.saasscheduler.admin.application.domain.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {
}
