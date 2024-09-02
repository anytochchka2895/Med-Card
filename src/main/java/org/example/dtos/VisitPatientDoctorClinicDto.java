package org.example.dtos;

import lombok.*;
import org.example.enums.VisitStatus;

import java.time.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitPatientDoctorClinicDto {

	private UUID visitId;
	private LocalDate visitDate;
	private LocalTime visitTime;
	private VisitStatus visitStatus;

	private UUID patientId;
	private String patientFullName;
	private LocalDate patientDateOfBirth;
	private Long patientPolicy;

	private UUID doctorId;
	private String doctorFullName;
	private String doctorSpecialty;
	private int doctorCabinet;

	private UUID clinicId;
	private String clinicName;
	private String clinicAddress;

	private String visitProblems;
	private String visitDiagnose;
	private String visitRecommendation;
	private ZonedDateTime visitCreatedAt;
	private ZonedDateTime visitUpdatedAt;

}
