package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class PatientEntity {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "policy")
	private Long policy;

	@Column(name = "address")
	private String address;

	@Column(name = "clinic_id")
	private UUID clinicId;

	@Column(name = "created_at")
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;


}
