package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.VisitStatus;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "visits")
public class VisitEntity {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "patient_id")
	private UUID patientId;

	@Column(name = "doctor_id")
	private UUID doctorId;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "time")
	private LocalTime time;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private VisitStatus status;

	@Column(name = "problems")
	private String problems;

	@Column(name = "diagnose")
	private String diagnose;

	@Column(name = "recommendation")
	private String recommendation;

	@Column(name = "created_at")
	private ZonedDateTime createdAt;

	@Column(name = "updated_at")
	private ZonedDateTime updatedAt;


}
