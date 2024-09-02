package org.example.dtos;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDto {
	private UUID id;
	private String fullName;
	private String specialty;
	private UUID clinicId;
	private int cabinet;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private Boolean deleted;
}
