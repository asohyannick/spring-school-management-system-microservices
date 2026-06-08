package com.ecol.admissionService.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "admissions")
public class Admission {
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private UUID id;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	@PrePersist
	public void prePersist()
	{
		createdAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdate()
	{
		createdAt = Instant.now();
		updatedAt = Instant.now();
	}
}
