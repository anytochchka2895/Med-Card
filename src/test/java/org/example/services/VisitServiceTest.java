package org.example.services;

import org.example.dtos.*;
import org.example.entities.*;
import org.example.enums.VisitStatus;
import org.example.exceptions.VisitException;
import org.example.repositories.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {
	@Mock VisitRepository visitRepository;
	@Mock ClinicRepository clinicRepository;
	@Mock DoctorRepository doctorRepository;
	@Mock PatientRepository patientRepository;
	VisitService visitService;

	EasyRandom generator = new EasyRandom();

	@BeforeEach
	void init() {
		visitService = new VisitService(visitRepository, clinicRepository, doctorRepository, patientRepository);
	}


	@Test
	void createVisitsForDoctorTest() {
		//GIVEN
		CreateVisitDto createVisitDto = generator.nextObject(CreateVisitDto.class);
		createVisitDto.setCountVisits(20);
		//MOCK

		//WHEN
		List<VisitDto> resultVisitsForDoctor = visitService.createVisitsForDoctor(createVisitDto);
		//THEN
		verify(visitRepository, times(1)).saveAll(any());
		assertEquals(createVisitDto.getCountVisits(), resultVisitsForDoctor.size());
		VisitDto firstVisitDto = resultVisitsForDoctor.get(0);
		assertEquals(createVisitDto.getDoctorId(), firstVisitDto.getDoctorId());
		assertEquals(createVisitDto.getDate(), firstVisitDto.getDate());
		resultVisitsForDoctor.forEach(visitDto -> assertEquals(VisitStatus.FREE, visitDto.getStatus()));
		assertNull(firstVisitDto.getPatientId());
		assertNull(firstVisitDto.getProblems());
		assertNull(firstVisitDto.getDiagnose());
		assertNull(firstVisitDto.getRecommendation());
	}


	@Test
	void getAllVisitsForDoctorBetweenDatesTest() {
		//GIVEN
		UUID doctorId = generator.nextObject(UUID.class);
		LocalDate startDate = generator.nextObject(LocalDate.class);
		LocalDate finishDate = generator.nextObject(LocalDate.class);
		List<VisitEntity> allVisitsByDoctorIdAndDateBetween = generator.objects(VisitEntity.class, 11).toList();
		//MOCK
		when(visitRepository
				     .findAllByDoctorIdAndDateBetween(doctorId, startDate, finishDate))
				.thenReturn(allVisitsByDoctorIdAndDateBetween);
		//WHEN
		List<VisitDto> resultList = visitService.getAllVisitsForDoctorBetweenDates(doctorId, startDate, finishDate);
		//THEN
		assertNotNull(resultList);
		assertEquals(allVisitsByDoctorIdAndDateBetween.size(), resultList.size());
	}


	@Test
	void getAllVisitsByStatusBookedTest() {
		//GIVEN
		UUID patientId = generator.nextObject(UUID.class);

		VisitStatus status = VisitStatus.BOOKED;
		List<VisitEntity> allByPatientIdAndStatus = generator.objects(VisitEntity.class, 11).toList();
		allByPatientIdAndStatus.forEach(visitEntity -> visitEntity.setPatientId(patientId));

		PatientEntity patientEntity = generator.nextObject(PatientEntity.class);
		patientEntity.setId(patientId);

		ClinicEntity clinicEntity = generator.nextObject(ClinicEntity.class);
		clinicEntity.setId(patientEntity.getClinicId());

		DoctorEntity doctorEntity = generator.nextObject(DoctorEntity.class);
		doctorEntity.setClinicId(clinicEntity.getId());
		allByPatientIdAndStatus.forEach(visitEntity -> visitEntity.setDoctorId(doctorEntity.getId()));
		//MOCK
		when(visitRepository.findAllByPatientIdAndStatus(patientId, status))
				.thenReturn(allByPatientIdAndStatus);
		when(patientRepository.findAllById(any()))
				.thenReturn(List.of(patientEntity));
		when(doctorRepository.findAllById(any()))
				.thenReturn(List.of(doctorEntity));
		when(clinicRepository.findAllById(any()))
				.thenReturn(List.of(clinicEntity));
		//WHEN
		List<VisitPatientDoctorClinicDto> resultList = visitService.getAllVisitsByStatus(patientId, status);
		//THEN
		assertNotNull(resultList);
		assertEquals(allByPatientIdAndStatus.size(), resultList.size());
		resultList.forEach((VisitPatientDoctorClinicDto visit) -> {
			                   assertEquals(patientEntity.getFullName(), visit.getPatientFullName());
			                   assertEquals(clinicEntity.getName(), visit.getClinicName());
			                   assertEquals(doctorEntity.getSpecialty(), visit.getDoctorSpecialty());
		                   });

//		for (VisitPatientDoctorClinicDto visit : resultList){
//			assertEquals(patientEntity.getFullName(), visit.getPatientFullName());
//			assertEquals(clinicEntity.getName(), visit.getClinicName());
//			assertEquals(doctorEntity.getSpecialty(), visit.getDoctorSpecialty());
//		}

	}


	@Test
	void getAllVisitsByStatusVisitedTest() {
		//GIVEN
		UUID patientId = generator.nextObject(UUID.class);

		VisitStatus status = VisitStatus.VISITED;
		List<VisitEntity> allByPatientIdAndStatus = generator.objects(VisitEntity.class, 11).toList();
		allByPatientIdAndStatus.forEach(visitEntity -> visitEntity.setPatientId(patientId));

		PatientEntity patientEntity = generator.nextObject(PatientEntity.class);
		patientEntity.setId(patientId);

		ClinicEntity clinicEntity = generator.nextObject(ClinicEntity.class);
		clinicEntity.setId(patientEntity.getClinicId());

		DoctorEntity doctorEntity = generator.nextObject(DoctorEntity.class);
		doctorEntity.setClinicId(clinicEntity.getId());
		allByPatientIdAndStatus.forEach(visitEntity -> visitEntity.setDoctorId(doctorEntity.getId()));
		//MOCK
		when(visitRepository.findAllByPatientIdAndStatus(patientId, status))
				.thenReturn(allByPatientIdAndStatus);
		when(patientRepository.findAllById(any()))
				.thenReturn(List.of(patientEntity));
		when(doctorRepository.findAllById(any()))
				.thenReturn(List.of(doctorEntity));
		when(clinicRepository.findAllById(any()))
				.thenReturn(List.of(clinicEntity));
		//WHEN
		List<VisitPatientDoctorClinicDto> resultList = visitService.getAllVisitsByStatus(patientId, status);
		//THEN
		assertNotNull(resultList);
		assertEquals(allByPatientIdAndStatus.size(), resultList.size());
		resultList.forEach((VisitPatientDoctorClinicDto visit) -> {
			assertEquals(patientEntity.getFullName(), visit.getPatientFullName());
			assertEquals(clinicEntity.getName(), visit.getClinicName());
			assertEquals(doctorEntity.getSpecialty(), visit.getDoctorSpecialty());
		});
	}


	@Test
	void getAllVisitsByStatusWithExceptionTest() {
		//GIVEN
		UUID patientId = generator.nextObject(UUID.class);
		VisitStatus status = VisitStatus.FREE;
		//MOCK

		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.getAllVisitsByStatus(patientId, status));
		//THEN
		assertTrue(thrown.getMessage().contains("Указан неверный статус"));
	}


	@Test
	void bookVisitWithEmptyIdTest() {
		//GIVEN
		BookVisitDto bookVisitDto = generator.nextObject(BookVisitDto.class);
		//MOCK
		when(visitRepository
				     .findById(bookVisitDto.getVisitId()))
				.thenReturn(Optional.empty());
		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.bookVisit(bookVisitDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По данному id ничего не найдено"));
	}


	@Test
	void bookVisitWithNotCorrectStatusTest() {
		//GIVEN
		BookVisitDto bookVisitDto = generator.nextObject(BookVisitDto.class);
		VisitEntity visitEntity = generator.nextObject(VisitEntity.class);
		visitEntity.setId(bookVisitDto.getVisitId());
		visitEntity.setStatus(VisitStatus.BOOKED);
		//MOCK
		when(visitRepository
				     .findById(bookVisitDto.getVisitId()))
				.thenReturn(Optional.of(visitEntity));
		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.bookVisit(bookVisitDto));
		//THEN
		assertTrue(thrown.getMessage().contains("Невозможно забронировать визит"));
	}


	@Test
	void bookVisitTest() {
		//GIVEN
		BookVisitDto bookVisitDto = generator.nextObject(BookVisitDto.class);
		VisitEntity visitEntity = generator.nextObject(VisitEntity.class);
		visitEntity.setId(bookVisitDto.getVisitId());
		visitEntity.setStatus(VisitStatus.FREE);
		//MOCK
		when(visitRepository
				     .findById(bookVisitDto.getVisitId()))
				.thenReturn(Optional.of(visitEntity));
		//WHEN
		VisitDto resultVisitDto = visitService.bookVisit(bookVisitDto);
		//THEN
		verify(visitRepository, times(1)).save(any());
		assertEquals(visitEntity.getId(), resultVisitDto.getId());
		assertEquals(bookVisitDto.getPatientId(), resultVisitDto.getPatientId());
		assertEquals(visitEntity.getDoctorId(), resultVisitDto.getDoctorId());
		assertEquals(VisitStatus.BOOKED, resultVisitDto.getStatus());
	}


	@Test
	void moveVisitWithEmptyOldIdTest() {
		//GIVEN
		MoveVisitDto moveVisitDto = generator.nextObject(MoveVisitDto.class);
		//MOCK
		when(visitRepository.findById(moveVisitDto.getOldVisitId()))
				.thenReturn(Optional.empty());
		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.moveVisit(moveVisitDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По старому id ничего не найдено"));
	}


	@Test
	void moveVisitWithEmptyNewIdTest() {
		//GIVEN
		MoveVisitDto moveVisitDto = generator.nextObject(MoveVisitDto.class);
		VisitEntity oldVisitEntity = generator.nextObject(VisitEntity.class);
		oldVisitEntity.setId(moveVisitDto.getOldVisitId());
		//MOCK
		when(visitRepository.findById(moveVisitDto.getOldVisitId()))
				.thenReturn(Optional.of(oldVisitEntity));
		when(visitRepository.findById(moveVisitDto.getNewVisitId()))
				.thenReturn(Optional.empty());
		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.moveVisit(moveVisitDto));
		//THEN
		assertTrue(thrown.getMessage().contains("По новому id ничего не найдено"));
		verify(visitRepository, times(1)).save(any());
	}


	@Test
	void moveVisitWithNotCorrectStatusTest() {
		//GIVEN
		MoveVisitDto moveVisitDto = generator.nextObject(MoveVisitDto.class);
		VisitEntity oldVisitEntity = generator.nextObject(VisitEntity.class);
		oldVisitEntity.setId(moveVisitDto.getOldVisitId());
		VisitEntity newVisitEntity = generator.nextObject(VisitEntity.class);
		newVisitEntity.setId(moveVisitDto.getNewVisitId());
		newVisitEntity.setStatus(VisitStatus.BOOKED);
		//MOCK
		when(visitRepository
				     .findById(moveVisitDto.getOldVisitId()))
				.thenReturn(Optional.of(oldVisitEntity));
		when(visitRepository
				     .findById(moveVisitDto.getNewVisitId()))
				.thenReturn(Optional.of(newVisitEntity));
		//WHEN
		VisitException thrown = assertThrows(
				VisitException.class,
				() -> visitService.moveVisit(moveVisitDto));
		//THEN
		assertTrue(thrown.getMessage().contains("Невозможно забронировать визит"));
		verify(visitRepository, times(1)).save(any());
	}


	@Test
	void moveVisitTest() {
		//GIVEN
		MoveVisitDto moveVisitDto = generator.nextObject(MoveVisitDto.class);
		VisitEntity oldVisitEntity = generator.nextObject(VisitEntity.class);
		oldVisitEntity.setId(moveVisitDto.getOldVisitId());
		VisitEntity newVisitEntity = generator.nextObject(VisitEntity.class);
		newVisitEntity.setId(moveVisitDto.getNewVisitId());
		newVisitEntity.setStatus(VisitStatus.FREE);
		//MOCK
		when(visitRepository
				     .findById(moveVisitDto.getOldVisitId()))
				.thenReturn(Optional.of(oldVisitEntity));
		when(visitRepository
				     .findById(moveVisitDto.getNewVisitId()))
				.thenReturn(Optional.of(newVisitEntity));
		//WHEN
		VisitDto resultVisitDto = visitService.moveVisit(moveVisitDto);
		//THEN
		verify(visitRepository, times(2)).save(any());
		assertEquals(VisitStatus.FREE, oldVisitEntity.getStatus());
		assertEquals(VisitStatus.BOOKED, newVisitEntity.getStatus());
		assertNull(oldVisitEntity.getPatientId());
		assertEquals(moveVisitDto.getPatientId(), resultVisitDto.getPatientId());
	}


	@Test
	void deleteVisitWithExceptionTest() {
		//GIVEN
		DeleteVisitDto deleteVisitDto = generator.nextObject(DeleteVisitDto.class);
		//MOCK
		when(visitRepository.findById(deleteVisitDto.getVisitId()))
				.thenReturn(Optional.empty());
		//WHEN
		VisitException exception = assertThrows(
				VisitException.class,
				() -> visitService.deleteVisit(deleteVisitDto));
		//THEN
		assertTrue(exception.getMessage().contains("По данному id ничего не найдено"));
	}

	@Test
	void deleteVisitTest() {
		//GIVEN
		DeleteVisitDto deleteVisitDto = generator.nextObject(DeleteVisitDto.class);
		VisitEntity visitEntity = generator.nextObject(VisitEntity.class);
		visitEntity.setId(deleteVisitDto.getVisitId());
		//MOCK
		when(visitRepository.findById(deleteVisitDto.getVisitId()))
				.thenReturn(Optional.of(visitEntity));
		//WHEN
		VisitDto resultVisitDto = visitService.deleteVisit(deleteVisitDto);
		//THEN
		verify(visitRepository, times(1)).save(any());
		assertEquals(deleteVisitDto.getVisitId(), resultVisitDto.getId());
		assertEquals(VisitStatus.FREE, resultVisitDto.getStatus());
		assertNull(resultVisitDto.getPatientId());
	}
}