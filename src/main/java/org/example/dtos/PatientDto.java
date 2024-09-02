package org.example.dtos;

import lombok.*;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {
	private UUID id;
	private String fullName;
	private LocalDate dateOfBirth;
	private Long policy;
	private String address;
	private UUID clinicId;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
}
