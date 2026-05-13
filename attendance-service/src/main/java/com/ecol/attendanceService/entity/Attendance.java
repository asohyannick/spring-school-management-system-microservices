package com.ecol.attendanceService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "attendances")
public class Attendance {
  @Id
  @GeneratedValue (strategy = GenerationType.UUID)
  private UUID id;
  
  private Instant created;
  private Instant updated;
  
  @PrePersist
   public void prePersist() {
    created = Instant.now();
   }
   
   @PreUpdate
   public void preUpdate() {
	  created = Instant.now();
	  updated = Instant.now();
   }
}
