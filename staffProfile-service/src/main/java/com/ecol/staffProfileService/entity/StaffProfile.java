package com.ecol.staffProfileService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "staff_profiles")
@Getter
@Setter
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class StaffProfile {
	@Id
	@GeneratedValue (strategy = GenerationType.UUID)
	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	
	private Instant createdAt;
	private Instant updatedAt;
}
