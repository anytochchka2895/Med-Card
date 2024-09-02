package org.example.mappers;

import org.example.dtos.PatientDto;
import org.example.entities.PatientEntity;

import java.util.List;

public class PatientMapper {
	public static PatientDto toPatientDto(PatientEntity patientEntity) {
		return new PatientDto(patientEntity.getId(),
		                      patientEntity.getFullName(),
		                      patientEntity.getDateOfBirth(),
		                      patientEntity.getPolicy(),
		                      patientEntity.getAddress(),
		                      patientEntity.getClinicId(),
		                      patientEntity.getCreatedAt(),
		                      patientEntity.getUpdatedAt());
	}


	public static List<PatientDto> toListPatientsDto(List<PatientEntity> patientEntity) {
		return patientEntity
				.stream()
				.map(PatientMapper::toPatientDto)
				.toList();
	}


}
