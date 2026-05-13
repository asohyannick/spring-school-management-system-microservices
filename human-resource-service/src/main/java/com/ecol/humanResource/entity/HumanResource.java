package com.ecol.humanResource.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "human-resource")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HumanResource {
 @Id
 @GeneratedValue (strategy = GenerationType.UUID)
 private UUID id;
 
 private Instant createdDate;
 private Instant lastModifiedDate;
 
 @PrePersist
 protected void onCreate() {
	 this.createdDate = Instant.now();
    this.lastModifiedDate = this.createdDate;
 }
 
 @PreUpdate
  protected void  onUpdate() {
	 this.lastModifiedDate = Instant.now ();
  }
 
}
