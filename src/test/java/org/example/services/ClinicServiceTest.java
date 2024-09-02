package org.example.services;

import org.example.dtos.ClinicDto;
import org.example.entities.ClinicEntity;
import org.example.exceptions.ClinicException;
import org.example.repositories.ClinicRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

	@Mock ClinicRepository clinicRepository;
	ClinicService clinicService;

	EasyRandom generator = new EasyRandom();

	@BeforeEach
	void init() {
		clinicService = new ClinicService(clinicRepository);
	}


	@Test
	void saveClinicNewClinicErrorClinicNameTest() {
    //GIVEN
		ClinicDto clinicDto = generator.nextObject(ClinicDto.class);
		clinicDto.setId(null);
		ClinicEntity clinicByName = generator.nextObject(ClinicEntity.class);
		clinicByName.setName(clinicDto.getName());
		//MOCK
		when(clinicRepository.findByName(clinicDto.getName())).thenReturn(clinicByName);
		//WHEN
		ClinicException thrown = assertThrows(
				ClinicException.class,
				() -> clinicService.saveClinic(clinicDto));
		//THEN
		assertTrue(thrown.getMessage().contains("Поликлиника с таким именем уже существует"));
	}


	@Test
	void saveClinicNewClinicTest() {
    //GIVEN
		ClinicDto clinicDto = generator.nextObject(ClinicDto.class);
		clinicDto.setId(null);
		//MOCK
		when(clinicRepository.findByName(clinicDto.getName())).thenReturn(null);
		//WHEN
		ClinicDto resultClinicDto = clinicService.saveClinic(clinicDto);
		//THEN
		verify(clinicRepository, times(1)).save(any());
		assertNotNull(resultClinicDto);
		assertNotNull(resultClinicDto.getId());
		assertNotNull(resultClinicDto.getCreatedAt());
		assertNotNull(resultClinicDto.getUpdatedAt());
		assertEquals(clinicDto.getName(), resultClinicDto.getName());
		assertEquals(clinicDto.getAddress(), resultClinicDto.getAddress());
	}


	@Test
	void saveClinicUpdateErrorClinicByIdIsEmptyTest() {
    //GIVEN
		ClinicDto clinicDto = generator.nextObject(ClinicDto.class);
		//MOCK
		when(clinicRepository.findById(clinicDto.getId())).thenReturn(Optional.empty());
		//WHEN
		ClinicException thrown = assertThrows(
				ClinicException.class,
				() -> clinicService.saveClinic(clinicDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По данному id ничего не найдено"));
	}


	@Test
	void saveClinicUpdateErrorClinicByNameTest() {
    //GIVEN
		ClinicDto clinicDto = generator.nextObject(ClinicDto.class);
		ClinicEntity clinicEntityById = generator.nextObject(ClinicEntity.class);
		clinicEntityById.setId(clinicDto.getId());
		ClinicEntity clinicEntityByName = generator.nextObject(ClinicEntity.class);
		//MOCK
		when(clinicRepository.findById(clinicDto.getId())).thenReturn(Optional.of(clinicEntityById));
		when(clinicRepository.findByName(clinicDto.getName())).thenReturn(clinicEntityByName);
		//WHEN
		ClinicException thrown = assertThrows(
				ClinicException.class,
				() -> clinicService.saveClinic(clinicDto));
		//THEN
		assertTrue(thrown.getMessage().contains("Клиника с данным именем уже существует"));
	}


	@Test
	void saveClinicUpdateClinicTest() {
    //GIVEN
		ClinicDto clinicDto = generator.nextObject(ClinicDto.class);
		ClinicEntity clinicEntityById = generator.nextObject(ClinicEntity.class);
		clinicEntityById.setId(clinicDto.getId());
		//MOCK
		when(clinicRepository.findById(clinicDto.getId())).thenReturn(Optional.of(clinicEntityById));
		when(clinicRepository.findByName(clinicDto.getName())).thenReturn(null);

		//WHEN
		ClinicDto resultClinicDto = clinicService.saveClinic(clinicDto);
		//THEN
		verify(clinicRepository, times(1)).save(any());
		assertNotNull(resultClinicDto);
		assertEquals(clinicDto.getId(), resultClinicDto.getId());
		assertEquals(clinicDto.getName(), resultClinicDto.getName());
		assertEquals(clinicDto.getAddress(), resultClinicDto.getAddress());
		assertEquals(clinicEntityById.getCreatedAt(), resultClinicDto.getCreatedAt());
	}

	@Test
	void getAllClinicsTest() {
		//GIVEN
		List<ClinicEntity> clinicEntityList = generator.objects(ClinicEntity.class, 10).toList();
		//MOCK
		when(clinicRepository.findAll()).thenReturn(clinicEntityList);
		//WHEN
		List<ClinicDto> resultClinics = clinicService.getAllClinics();
		//THEN
		assertNotNull(resultClinics);
		assertEquals(clinicEntityList.size(), resultClinics.size());
	}


	//GIVEN

	//MOCK

	//WHEN

	//THEN

}