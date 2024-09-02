package org.example.dtos;

import lombok.*;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVisitDto {
	UUID doctorId;
	LocalDate date;
	LocalTime time;
	int countVisits;
}
