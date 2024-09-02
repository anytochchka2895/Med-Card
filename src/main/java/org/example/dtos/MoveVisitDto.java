package org.example.dtos;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoveVisitDto {
	UUID oldVisitId;
	UUID newVisitId;
	UUID patientId;
}
