package com.ecol.paymentService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
 @Id
 @GeneratedValue (strategy= GenerationType.UUID)
 private UUID id;
 
 private String paymentId;
 private String paymentType;
 private String paymentStatus;
 private BigDecimal paymentAmount;
 private Instant createdDate;
 private Instant updatedDate;
 
 @PrePersist
 protected  void onCreate() {
	 Instant now = Instant.now();
	 this.createdDate = now;
	 this.updatedDate = now;
 }
 
 @PreUpdate
 protected void onUpdate() {
	 Instant now = Instant.now();
	 this.updatedDate = now;
 }
 
 
}
