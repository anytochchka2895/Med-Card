package org.example.mappers;

import org.example.dtos.DoctorDto;
import org.example.entities.DoctorEntity;

import java.util.List;

public class DoctorMapper {
	public static DoctorDto toDoctorDto(DoctorEntity doctorEntity) {
		return new DoctorDto(doctorEntity.getId(),
		                     doctorEntity.getFullName(),
		                     doctorEntity.getSpecialty(),
		                     doctorEntity.getClinicId(),
		                     doctorEntity.getCabinet(),
		                     doctorEntity.getCreatedAt(),
		                     doctorEntity.getUpdatedAt(),
		                     doctorEntity.getDeleted());
	}


	public static List<DoctorDto> toListDoctorsDto(List<DoctorEntity> doctorsEntity) {
		return doctorsEntity
				.stream()
				.map(DoctorMapper::toDoctorDto)
				.toList();
	}


}
