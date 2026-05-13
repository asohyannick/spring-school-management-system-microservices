package com.ecol.inventoryService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Inventory {
  
  @Id
  private UUID id;
  @Column
  private String name;
  @Column
  private String description;
  
  private Instant createdAt;
  private Instant updatedAt;
  
  @PrePersist
 protected void onCreate() {
	  createdAt = Instant.now();
	  updatedAt = Instant.now();
  }
  
  @PreUpdate
  protected void onUpdate() {
	  updatedAt = Instant.now();
  }
}
