package org.example.dtos;

import lombok.*;
import org.example.enums.VisitStatus;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDto {
	private UUID id;
	private UUID patientId;
	private UUID doctorId;
	private LocalDate date;
	private LocalTime time;
	private VisitStatus status;
	private String problems;
	private String diagnose;
	private String recommendation;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
}
