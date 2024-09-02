package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.ClinicDto;
import org.example.entities.ClinicEntity;
import org.example.exceptions.ClinicException;
import org.example.mappers.ClinicMapper;
import org.example.repositories.ClinicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ClinicService {
	private final ClinicRepository clinicRepository;


	@Transactional
	public ClinicDto saveClinic(ClinicDto clinicDto) {
		if (Objects.isNull(clinicDto.getId())) {
			return newClinic(clinicDto);
		}
		else {
			return updateClinic(clinicDto);
		}
	}


	private ClinicDto newClinic(ClinicDto clinicDto) {
		ClinicEntity clinicByName = clinicRepository.findByName(clinicDto.getName());
		if (Objects.nonNull(clinicByName)) {
			throw new ClinicException("Поликлиника с таким именем уже существует");
		}
		ClinicEntity clinicEntity = new ClinicEntity(UUID.randomUUID(),
		                                             clinicDto.getName(),
		                                             clinicDto.getAddress(),
		                                             ZonedDateTime.now(),
		                                             ZonedDateTime.now());
		clinicRepository.save(clinicEntity);
		return ClinicMapper.toClinicDto(clinicEntity);
	}


	private ClinicDto updateClinic(ClinicDto clinicDto) {
		ClinicEntity clinicEntityById = clinicRepository
				.findById(clinicDto.getId())
				.orElseThrow(() -> new ClinicException("По данному id ничего не найдено"));

		if(!clinicDto.getName().equals(clinicEntityById.getName())) {
			if (Objects.nonNull(clinicRepository.findByName(clinicDto.getName()))) {
				throw new ClinicException("Клиника с данным именем уже существует");
			}
		}
		clinicEntityById.setName(clinicDto.getName());
		clinicEntityById.setAddress(clinicDto.getAddress());
		clinicEntityById.setUpdatedAt(ZonedDateTime.now());
		clinicRepository.save(clinicEntityById);
		return ClinicMapper.toClinicDto(clinicEntityById);
	}


	public List<ClinicDto> getAllClinics() {
		List<ClinicEntity> allClinics = clinicRepository.findAll();
		return ClinicMapper.toListClinicsDto(allClinics);
	}



}
