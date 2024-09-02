package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.PatientDto;
import org.example.entities.PatientEntity;
import org.example.exceptions.PatientException;
import org.example.mappers.PatientMapper;
import org.example.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PatientService {
	private final PatientRepository patientRepository;


	@Transactional
	public PatientDto savePatient(PatientDto patientDto) {
		if (Objects.isNull(patientDto.getId())) {
			return newPatient(patientDto);
		}
		else {
			return updatePatient(patientDto);
		}
	}


	private PatientDto newPatient(PatientDto patientDto) {
		PatientEntity patientByPolicy = patientRepository.findByPolicy(patientDto.getPolicy());
		if (Objects.nonNull(patientByPolicy)) {
			throw new PatientException("Пациент с таким полисом уже существует");
		}
		PatientEntity patientEntity = new PatientEntity(UUID.randomUUID(),
		                                                patientDto.getFullName(),
		                                                patientDto.getDateOfBirth(),
		                                                patientDto.getPolicy(),
		                                                patientDto.getAddress(),
		                                                patientDto.getClinicId(),
		                                                ZonedDateTime.now(),
		                                                ZonedDateTime.now());
		patientRepository.save(patientEntity);
		return PatientMapper.toPatientDto(patientEntity);
	}


	private PatientDto updatePatient(PatientDto patientDto) {
		PatientEntity patientEntityById = patientRepository
				.findById(patientDto.getId())
				.orElseThrow(() -> new PatientException("По данному id ничего не найдено"));
		patientEntityById.setAddress(patientDto.getAddress());
		patientEntityById.setUpdatedAt(ZonedDateTime.now());
		patientRepository.save(patientEntityById);
		return PatientMapper.toPatientDto(patientEntityById);
	}


}
