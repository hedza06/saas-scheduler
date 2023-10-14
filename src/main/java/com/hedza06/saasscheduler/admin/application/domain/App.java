package com.hedza06.saasscheduler.admin.application.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "app")
public class App implements Serializable {
  @Id
  @GeneratedValue(generator = "app_id_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "app_id_seq",
      sequenceName = "app_id_seq",
      allocationSize = 1
  )
  private Integer id;

  private String name;

  @Column(unique = true)
  private String username;

  @ToString.Exclude
  private String password;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = false;
}
