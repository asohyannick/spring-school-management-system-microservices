package com.ecol.studentProfileService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student_profiles")
@RequiredArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Data
@Builder
public class StudentProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  private Instant createdAt;
  private Instant updatedAt;
  
  @PrePersist
  protected  void onCreate() {
	  Instant now = Instant.now();
	  this.createdAt = now;
  }
  
  @PreUpdate
  protected  void onUpdate() {
	  Instant now = Instant.now();
	  this.createdAt = now;
	  this.updatedAt = now;
  }
}
