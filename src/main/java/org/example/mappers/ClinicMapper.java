package org.example.mappers;

import org.example.dtos.ClinicDto;
import org.example.entities.ClinicEntity;

import java.util.List;

public class ClinicMapper {
	public static ClinicDto toClinicDto(ClinicEntity clinicEntity) {
		return new ClinicDto(clinicEntity.getId(),
		                     clinicEntity.getName(),
		                     clinicEntity.getAddress(),
		                     clinicEntity.getCreatedAt(),
		                     clinicEntity.getUpdatedAt());
	}


	public static List<ClinicDto> toListClinicsDto(List<ClinicEntity> clinicsEntity) {
		return clinicsEntity
				.stream()
				.map(ClinicMapper::toClinicDto)
				.toList();
	}


}
