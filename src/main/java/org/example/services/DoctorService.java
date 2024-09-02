package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.DoctorDto;
import org.example.entities.DoctorEntity;
import org.example.enums.VisitStatus;
import org.example.exceptions.DoctorException;
import org.example.mappers.DoctorMapper;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

import static org.example.enums.VisitStatus.*;

@RequiredArgsConstructor
@Service
public class DoctorService {
	private final DoctorRepository doctorRepository;
	private final VisitRepository visitRepository;


	@Transactional
	public DoctorDto saveDoctor(DoctorDto doctorDto) {
		if (Objects.isNull(doctorDto.getId())) {
			return newDoctor(doctorDto);
		}
		else {
			return updateDoctor(doctorDto);
		}
	}


	private DoctorDto newDoctor(DoctorDto doctorDto) {
		DoctorEntity doctorEntity = new DoctorEntity(UUID.randomUUID(),
		                                             doctorDto.getFullName(),
		                                             doctorDto.getSpecialty(),
		                                             doctorDto.getClinicId(),
		                                             doctorDto.getCabinet(),
		                                             ZonedDateTime.now(),
		                                             ZonedDateTime.now(),
		                                             false);
		doctorRepository.save(doctorEntity);
		return DoctorMapper.toDoctorDto(doctorEntity);
	}


	private DoctorDto updateDoctor(DoctorDto doctorDto) {
		DoctorEntity doctorEntityById = doctorRepository
				.findById(doctorDto.getId())
				.orElseThrow(() -> new DoctorException("По данному id ничего не найдено"));
		doctorEntityById.setFullName(doctorDto.getFullName());
		doctorEntityById.setSpecialty(doctorDto.getSpecialty());
		doctorEntityById.setClinicId(doctorDto.getClinicId());
		doctorEntityById.setCabinet(doctorDto.getCabinet());
		doctorEntityById.setUpdatedAt(ZonedDateTime.now());
		doctorRepository.save(doctorEntityById);
		return DoctorMapper.toDoctorDto(doctorEntityById);
	}


	public List<DoctorDto> getAllActiveDoctors() {
		List<DoctorEntity> allActiveDoctors = doctorRepository.findAllByDeletedIsFalse();
		return DoctorMapper.toListDoctorsDto(allActiveDoctors);
	}


	public List<DoctorDto> getAllActiveDoctorsByClinicId(UUID clinicId) {
		List<DoctorEntity> allActiveDoctorsByClinicId = doctorRepository.findAllByClinicIdAndDeletedIsFalse(clinicId);
		return DoctorMapper.toListDoctorsDto(allActiveDoctorsByClinicId);
	}


	@Transactional
	public DoctorDto deleteDoctor(UUID doctorId) {
		List<VisitStatus> visitStatusList = List.of(FREE, BOOKED);
		DoctorEntity doctorEntityById = doctorRepository
				.findById(doctorId)
				.orElseThrow(() -> new DoctorException("По данному id ничего не найдено"));
		doctorEntityById.setDeleted(true);
		doctorRepository.save(doctorEntityById);
		visitRepository.deleteFutureVisitsWithDoctorId(doctorId, visitStatusList);
		return DoctorMapper.toDoctorDto(doctorEntityById);
	}


}
