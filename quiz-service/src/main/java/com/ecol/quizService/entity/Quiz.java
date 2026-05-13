package com.ecol.quizService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Quiz {
    @Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private UUID id;
	
	private Instant createdAt;
	private Instant updatedAt;
	
	@PrePersist
    protected void onCreate() {
		this.createdAt = Instant.now();
	}
	
	@PreUpdate
    protected void onUpdate() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
    }
}
