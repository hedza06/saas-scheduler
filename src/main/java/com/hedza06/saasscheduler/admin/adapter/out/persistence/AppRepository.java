package com.hedza06.saasscheduler.admin.adapter.out.persistence;

import com.hedza06.saasscheduler.admin.application.domain.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {
  @Query(value = "select app.id from App app where app.username = :username")
  Integer findIdByUsername(@Param("username") String username);

  @Query(value = """
      select app.name
      from AdminToken adminToken
      join adminToken.app app
      where adminToken.token = :token
      and adminToken.isActive = true""")
  List<String> findNameByAdminToken(@Param("token") String token);
}
