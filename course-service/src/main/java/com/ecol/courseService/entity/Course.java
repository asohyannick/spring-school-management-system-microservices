package com.ecol.courseService.entity;

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
public class Course {
   
   @Id
   @GeneratedValue (strategy = GenerationType.UUID)
   private UUID id;
   
   private Instant createdAt;
   private Instant updatedAt;
   
   @PrePersist
   protected void onCreate() {
	   Instant now = Instant.now();
	   this.createdAt = now;
   }
   
   @PreUpdate
   protected void onUpdate() {
	   Instant now = Instant.now();
	   this.updatedAt = now;
	   this.createdAt = now;
   }
}
