package com.hedza06.saasscheduler.admin.adapter.out.persistence;

import com.hedza06.saasscheduler.admin.application.domain.AdminToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTokenRepository extends JpaRepository<AdminToken, Integer> {
  @Query(value = "select adminToken from AdminToken adminToken " +
      "left join fetch adminToken.app app " +
      "where app.username = :username")
  AdminToken findByUsernameWithApp(@Param("username") String username);

  boolean existsByToken(String token);
}
