package org.example.services;

import org.example.dtos.DoctorDto;
import org.example.entities.DoctorEntity;
import org.example.enums.VisitStatus;
import org.example.exceptions.DoctorException;
import org.example.repositories.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.example.enums.VisitStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

	@Mock DoctorRepository doctorRepository;
	@Mock VisitRepository visitRepository;
	DoctorService doctorService;

	EasyRandom generator = new EasyRandom();

	@BeforeEach
	void init() {
		doctorService = new DoctorService(doctorRepository, visitRepository);
	}


	@Test
	void saveDoctorNewDoctorTest() {
		//GIVEN
		DoctorDto doctorDto = generator.nextObject(DoctorDto.class);
		doctorDto.setId(null);
		//MOCK

		//WHEN
		DoctorDto resultDoctorDto = doctorService.saveDoctor(doctorDto);
		//THEN
		verify(doctorRepository, times(1)).save(any());
		assertNotNull(resultDoctorDto);
		assertNotNull(resultDoctorDto.getId());
		assertNotNull(resultDoctorDto.getCreatedAt());
		assertNotNull(resultDoctorDto.getUpdatedAt());
		assertFalse(resultDoctorDto.getDeleted());
		assertEquals(doctorDto.getFullName(), resultDoctorDto.getFullName());
		assertEquals(doctorDto.getSpecialty(), resultDoctorDto.getSpecialty());
		assertEquals(doctorDto.getClinicId(), resultDoctorDto.getClinicId());
		assertEquals(doctorDto.getCabinet(), resultDoctorDto.getCabinet());
	}


	@Test
	void saveDoctorUpdateErrorDoctorByIdIsEmptyTest() {
		//GIVEN
		DoctorDto doctorDto = generator.nextObject(DoctorDto.class);
		//MOCK
		when(doctorRepository.findById(doctorDto.getId())).thenReturn(Optional.empty());
		//WHEN
		DoctorException thrown = assertThrows(
				DoctorException.class,
				() -> doctorService.saveDoctor(doctorDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По данному id ничего не найдено"));
	}


	@Test
	void saveDoctorUpdateDoctorTest() {
		//GIVEN
		DoctorDto doctorDto = generator.nextObject(DoctorDto.class);
		DoctorEntity doctorEntityById = generator.nextObject(DoctorEntity.class);
		doctorEntityById.setId(doctorDto.getId());
		//MOCK
		when(doctorRepository.findById(doctorDto.getId())).thenReturn(Optional.of(doctorEntityById));
		//WHEN
		DoctorDto resultDoctorDto = doctorService.saveDoctor(doctorDto);
		//THEN
		verify(doctorRepository, times(1)).save(any());
		assertNotNull(resultDoctorDto);
		assertEquals(doctorDto.getId(), resultDoctorDto.getId());
		assertEquals(doctorDto.getFullName(), resultDoctorDto.getFullName());
		assertEquals(doctorDto.getSpecialty(), resultDoctorDto.getSpecialty());
		assertEquals(doctorDto.getClinicId(), resultDoctorDto.getClinicId());
		assertEquals(doctorDto.getCabinet(), resultDoctorDto.getCabinet());
		assertEquals(doctorEntityById.getCreatedAt(), resultDoctorDto.getCreatedAt());
	}


	@Test
	void getAllActiveDoctorsTest() {
		//GIVEN
		List<DoctorEntity> doctorEntityList = generator.objects(DoctorEntity.class, 10).toList();
		//MOCK
		when(doctorRepository.findAllByDeletedIsFalse()).thenReturn(doctorEntityList);
		//WHEN
		List<DoctorDto> resultActiveDoctors = doctorService.getAllActiveDoctors();
		//THEN
		assertNotNull(resultActiveDoctors);
		assertEquals(doctorEntityList.size(), resultActiveDoctors.size());
	}


	@Test
	void getAllActiveDoctorsByClinicIdTest() {
		//GIVEN
		List<DoctorEntity> doctorEntityList = generator.objects(DoctorEntity.class, 10).toList();
		UUID clinicId = generator.nextObject(UUID.class);
		doctorEntityList.forEach(doctorEntity -> doctorEntity.setClinicId(clinicId));
		//MOCK
		when(doctorRepository.findAllByClinicIdAndDeletedIsFalse(clinicId)).thenReturn(doctorEntityList);
		//WHEN
		List<DoctorDto> resultDoctors = doctorService.getAllActiveDoctorsByClinicId(clinicId);
		//THEN
		assertNotNull(resultDoctors);
		assertEquals(doctorEntityList.size(), resultDoctors.size());
	}


	@Test
	void deleteDoctorWithExceptionTest() {
		//GIVEN
		UUID doctorId = generator.nextObject(UUID.class);
		//MOCK
		when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
		//WHEN
		DoctorException thrown = assertThrows(
				DoctorException.class,
				() -> doctorService.deleteDoctor(doctorId));
		//THEN
		assertTrue(thrown.getMessage().contains("По данному id ничего не найдено"));
	}


	@Test
	void deleteDoctorTest() {
		//GIVEN
		UUID doctorId = generator.nextObject(UUID.class);
		DoctorEntity doctorEntityById = generator.nextObject(DoctorEntity.class);
		doctorEntityById.setId(doctorId);
		List<VisitStatus> visitStatusList = List.of(FREE, BOOKED);
		//MOCK
		when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctorEntityById));
		//WHEN
		DoctorDto resultDoctorDto = doctorService.deleteDoctor(doctorId);
		//THEN
		verify(doctorRepository, times(1)).save(any());
		verify(visitRepository, times(1)).deleteFutureVisitsWithDoctorId(doctorId, visitStatusList);
		assertNotNull(resultDoctorDto);
		assertEquals(doctorEntityById.getId(), resultDoctorDto.getId());
		assertEquals(doctorEntityById.getFullName(), resultDoctorDto.getFullName());
		assertEquals(doctorEntityById.getSpecialty(), resultDoctorDto.getSpecialty());
		assertEquals(doctorEntityById.getClinicId(), resultDoctorDto.getClinicId());
		assertEquals(doctorEntityById.getCabinet(), resultDoctorDto.getCabinet());
		assertEquals(doctorEntityById.getCreatedAt(), resultDoctorDto.getCreatedAt());
		assertTrue(resultDoctorDto.getDeleted());
	}
}