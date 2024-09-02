package org.example.services;

import org.example.dtos.PatientDto;
import org.example.entities.PatientEntity;
import org.example.exceptions.PatientException;
import org.example.repositories.PatientRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

	@Mock PatientRepository patientRepository;
	PatientService patientService;

	EasyRandom generator = new EasyRandom();

	@BeforeEach
	void init() {
		patientService = new PatientService(patientRepository);
	}


	@Test
	void savePatientNewClinicErrorPolicyNumberTest() {
		//GIVEN
		PatientDto patientDto = generator.nextObject(PatientDto.class);
		patientDto.setId(null);
		PatientEntity patientByPolicy = generator.nextObject(PatientEntity.class);
		patientByPolicy.setPolicy(patientDto.getPolicy());
		//MOCK
		when(patientRepository.findByPolicy(patientDto.getPolicy())).thenReturn(patientByPolicy);
		//WHEN
		PatientException thrown = assertThrows(
				PatientException.class,
				() -> patientService.savePatient(patientDto));
		//THEN
		assertTrue(thrown.getMessage().contains("Пациент с таким полисом уже существует"));
	}


	@Test
	void savePatientNewPatientTest() {
		//GIVEN
		PatientDto patientDto = generator.nextObject(PatientDto.class);
		patientDto.setId(null);
		//MOCK
		when(patientRepository.findByPolicy(patientDto.getPolicy())).thenReturn(null);
		//WHEN
		PatientDto resultPatientDto = patientService.savePatient(patientDto);
		//THEN
		verify(patientRepository, times(1)).save(any());
		assertNotNull(resultPatientDto);
		assertNotNull(resultPatientDto.getId());
		assertNotNull(resultPatientDto.getCreatedAt());
		assertNotNull(resultPatientDto.getUpdatedAt());
		assertEquals(patientDto.getFullName(), resultPatientDto.getFullName());
		assertEquals(patientDto.getDateOfBirth(), resultPatientDto.getDateOfBirth());
		assertEquals(patientDto.getPolicy(), resultPatientDto.getPolicy());
		assertEquals(patientDto.getAddress(), resultPatientDto.getAddress());
		assertEquals(patientDto.getClinicId(), resultPatientDto.getClinicId());
	}


	@Test
	void savePatientUpdateErrorPatientByIdIsEmptyTest() {
		//GIVEN
		PatientDto patientDto = generator.nextObject(PatientDto.class);
		//MOCK
		when(patientRepository.findById(patientDto.getId())).thenReturn(Optional.empty());
		//WHEN
		PatientException thrown = assertThrows(
				PatientException.class,
				() -> patientService.savePatient(patientDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По данному id ничего не найдено"));
	}


	@Test
	void savePatientUpdatePatientTest() {
		//GIVEN
		PatientDto patientDto = generator.nextObject(PatientDto.class);
		PatientEntity patientEntityById = generator.nextObject(PatientEntity.class);
		patientEntityById.setId(patientDto.getId());
		//MOCK
		when(patientRepository.findById(patientDto.getId())).thenReturn(Optional.of(patientEntityById));
		//WHEN
		PatientDto resultPatientDto = patientService.savePatient(patientDto);
		//THEN
		verify(patientRepository, times(1)).save(any());
		assertNotNull(resultPatientDto);
		assertEquals(patientDto.getId(), resultPatientDto.getId());
		assertEquals(patientEntityById.getFullName(), resultPatientDto.getFullName());
		assertEquals(patientEntityById.getDateOfBirth(), resultPatientDto.getDateOfBirth());
		assertEquals(patientEntityById.getPolicy(), resultPatientDto.getPolicy());
		assertEquals(patientDto.getAddress(), resultPatientDto.getAddress());
		assertEquals(patientEntityById.getClinicId(), resultPatientDto.getClinicId());
		assertEquals(patientEntityById.getCreatedAt(), resultPatientDto.getCreatedAt());
	}
}