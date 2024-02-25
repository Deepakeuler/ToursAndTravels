package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {

  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @JsonIgnore
  private Long id;

  @Column(name = "created_at")
  @CreationTimestamp
  @JsonIgnore
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  @JsonIgnore
  private LocalDateTime updatedAt;

  @Version
  @Column(name = "version")
  @JsonIgnore
  private Long version;

  @Column(name = "deleted")
  @JsonIgnore
  private Boolean deleted = false;
}