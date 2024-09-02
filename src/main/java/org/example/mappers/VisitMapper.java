package org.example.mappers;

import org.example.dtos.VisitDto;
import org.example.entities.VisitEntity;

import java.util.List;

public class VisitMapper {

	public static VisitDto visitToDto(VisitEntity visitEntity) {
		return VisitDto
				.builder()
				.id(visitEntity.getId())
				.patientId(visitEntity.getPatientId())
				.doctorId(visitEntity.getDoctorId())
				.date(visitEntity.getDate())
				.time(visitEntity.getTime())
				.status(visitEntity.getStatus())
				.problems(visitEntity.getProblems())
				.diagnose(visitEntity.getDiagnose())
				.recommendation(visitEntity.getRecommendation())
				.createdAt(visitEntity.getCreatedAt())
				.updatedAt(visitEntity.getUpdatedAt())
				.build();
	}


	public static List<VisitDto> visitToListDtos(List<VisitEntity> visitEntities) {
		return visitEntities.stream()
		                    .map(VisitMapper::visitToDto)
		                    .toList();
	}




}
