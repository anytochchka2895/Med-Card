package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
public class DoctorEntity {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "specialty")
	private String specialty;

	@Column(name = "clinic_id")
	private UUID clinicId;

	@Column(name = "cabinet")
	private int cabinet;

	@Column(name = "created_at")
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;

	@Column(name = "deleted")
	private Boolean deleted;




}
