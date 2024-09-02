package org.example.dtos;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClinicDto {
	private UUID id;
	private String name;
	private String address;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;


}
