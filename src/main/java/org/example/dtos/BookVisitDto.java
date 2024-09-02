package org.example.dtos;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookVisitDto {
	UUID visitId;
	UUID patientId;
}
