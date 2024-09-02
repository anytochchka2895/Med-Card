package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.*;
import org.example.entities.*;
import org.example.enums.VisitStatus;
import org.example.exceptions.VisitException;
import org.example.mappers.VisitMapper;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VisitService {
	private final VisitRepository visitRepository;
	private final ClinicRepository clinicRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;

	@Transactional
	public List<VisitDto> createVisitsForDoctor(CreateVisitDto createVisitDto) {
		List<VisitEntity> visitEntities = new ArrayList<>();
		int visitLong = 20;
		for (int i = 0; i < createVisitDto.getCountVisits(); i++) {
			VisitEntity visitEntity = visitEntityBuilder(createVisitDto);
			visitEntities.add(visitEntity);
			createVisitDto.setTime(createVisitDto.getTime().plusMinutes(visitLong));
		}
		visitRepository.saveAll(visitEntities);
		return VisitMapper.visitToListDtos(visitEntities);
	}


	private VisitEntity visitEntityBuilder(CreateVisitDto createVisitDto) {
		return VisitEntity.builder()
		                  .id(UUID.randomUUID())
		                  .patientId(null)
		                  .doctorId(createVisitDto.getDoctorId())
		                  .date(createVisitDto.getDate())
		                  .time(createVisitDto.getTime())
		                  .status(VisitStatus.FREE)
		                  .problems(null)
		                  .diagnose(null)
		                  .recommendation(null)
		                  .createdAt(ZonedDateTime.now())
		                  .updatedAt(ZonedDateTime.now())
		                  .build();
	}


	public List<VisitDto> getAllVisitsForDoctorBetweenDates(UUID doctorId,
	                                                        LocalDate startDate,
	                                                        LocalDate finishDate) {
		List<VisitEntity> allVisitsByDoctorIdAndDateBetween = visitRepository
				.findAllByDoctorIdAndDateBetween(doctorId, startDate, finishDate);
		return VisitMapper.visitToListDtos(allVisitsByDoctorIdAndDateBetween);
	}


	public List<VisitPatientDoctorClinicDto> getAllVisitsByStatus(UUID patientId,
	                                                              VisitStatus status) {

		if (status.equals(VisitStatus.BOOKED)) {
			List<VisitEntity> allByPatientIdAndStatusBooked = visitRepository
					.findAllByPatientIdAndStatus(patientId, status);

			return mappers(allByPatientIdAndStatusBooked);

		}

		if (status.equals(VisitStatus.VISITED)) {
			List<VisitEntity> allByPatientIdAndStatusVisited = visitRepository
					.findAllByPatientIdAndStatus(patientId, status);

			return mappers(allByPatientIdAndStatusVisited);
		}

		else {
			throw new VisitException("Указан неверный статус");
		}
	}


	private List<VisitPatientDoctorClinicDto> mappers(List<VisitEntity> allByPatientIdAndStatus) {
		List<UUID> idsPatients = allByPatientIdAndStatus
				.stream()
				.map(VisitEntity::getPatientId)
				.toList();
		Map<UUID, PatientEntity> allPatientsMap = patientRepository
				.findAllById(idsPatients)
				.stream()
				.collect(Collectors.toMap(PatientEntity::getId, patientEntity -> patientEntity));

		List<UUID> idsDoctors = allByPatientIdAndStatus
				.stream()
				.map(VisitEntity::getDoctorId)
				.toList();
		List<DoctorEntity> allDoctorsByIds = doctorRepository.findAllById(idsDoctors);
		Map<UUID, DoctorEntity> allDoctorsMap = allDoctorsByIds
				.stream()
				.collect(Collectors.toMap(DoctorEntity::getId, doctorEntity -> doctorEntity));

		List<UUID> idsClinics = allDoctorsByIds
				.stream()
				.map(DoctorEntity::getClinicId)
				.toList();
		Map<UUID, ClinicEntity> allClinicsMap = clinicRepository
				.findAllById(idsClinics)
				.stream()
				.collect(Collectors.toMap(ClinicEntity::getId, clinicEntity -> clinicEntity));

		return allByPatientIdAndStatus
				.stream()
				.map(visitEntity -> buildVPDCDto(visitEntity,
				                                 allPatientsMap,
				                                 allDoctorsMap,
				                                 allClinicsMap))
				.toList();

	}

	private VisitPatientDoctorClinicDto buildVPDCDto(VisitEntity visitEntity,
	                                                 Map<UUID, PatientEntity> allPatientsMap,
	                                                 Map<UUID, DoctorEntity> allDoctorsMap,
	                                                 Map<UUID, ClinicEntity> allClinicsMap) {
		UUID patientId = visitEntity.getPatientId();
		PatientEntity patientEntity = allPatientsMap.get(patientId);
		UUID doctorId = visitEntity.getDoctorId();
		DoctorEntity doctorEntity = allDoctorsMap.get(doctorId);
		UUID clinicId = doctorEntity.getClinicId();
		ClinicEntity clinicEntity = allClinicsMap.get(clinicId);

		return VisitPatientDoctorClinicDto
				.builder()
				.visitId(visitEntity.getId())
				.visitDate(visitEntity.getDate())
				.visitTime(visitEntity.getTime())
				.visitStatus(visitEntity.getStatus())

				.patientId(patientId)
				.patientFullName(patientEntity.getFullName())
				.patientDateOfBirth(patientEntity.getDateOfBirth())
				.patientPolicy(patientEntity.getPolicy())

				.doctorId(doctorId)
				.doctorFullName(doctorEntity.getFullName())
				.doctorSpecialty(doctorEntity.getSpecialty())
				.doctorCabinet(doctorEntity.getCabinet())

				.clinicId(clinicId)
				.clinicName(clinicEntity.getName())
				.clinicAddress(clinicEntity.getAddress())

				.visitProblems(visitEntity.getProblems())
				.visitDiagnose(visitEntity.getDiagnose())
				.visitRecommendation(visitEntity.getRecommendation())
				.visitCreatedAt(visitEntity.getCreatedAt())
				.visitUpdatedAt(visitEntity.getUpdatedAt())

				.build();
	}


	@Transactional
	public VisitDto bookVisit(BookVisitDto bookVisitDto) {
		VisitEntity visitEntity = visitRepository
				.findById(bookVisitDto.getVisitId())
				.orElseThrow(() -> new VisitException("По данному id ничего не найдено"));
		if (!visitEntity.getStatus().equals(VisitStatus.FREE)) {
			throw new VisitException("Невозможно забронировать визит");
		}
		visitEntity.setStatus(VisitStatus.BOOKED);
		visitEntity.setPatientId(bookVisitDto.getPatientId());
		visitEntity.setUpdatedAt(ZonedDateTime.now());
		visitRepository.save(visitEntity);
		return VisitMapper.visitToDto(visitEntity);
	}

	@Transactional
	public VisitDto moveVisit(MoveVisitDto moveVisitDto) {
		VisitEntity oldVisitEntity = visitRepository
				.findById(moveVisitDto.getOldVisitId())
				.orElseThrow(() -> new VisitException("По старому id ничего не найдено"));
		oldVisitEntity.setStatus(VisitStatus.FREE);
		oldVisitEntity.setPatientId(null);
		oldVisitEntity.setUpdatedAt(ZonedDateTime.now());
		visitRepository.save(oldVisitEntity);

		VisitEntity newVisitEntity = visitRepository
				.findById(moveVisitDto.getNewVisitId())
				.orElseThrow(() -> new VisitException("По новому id ничего не найдено"));
		if (!newVisitEntity.getStatus().equals(VisitStatus.FREE)) {
			throw new VisitException("Невозможно забронировать визит");
		}
		newVisitEntity.setStatus(VisitStatus.BOOKED);
		newVisitEntity.setPatientId(moveVisitDto.getPatientId());
		newVisitEntity.setUpdatedAt(ZonedDateTime.now());
		visitRepository.save(newVisitEntity);
		return VisitMapper.visitToDto(newVisitEntity);
	}

	@Transactional
	public VisitDto deleteVisit(DeleteVisitDto deleteVisitDto) {
		VisitEntity visitEntity = visitRepository
				.findById(deleteVisitDto.getVisitId())
				.orElseThrow(() -> new VisitException("По данному id ничего не найдено"));
		visitEntity.setStatus(VisitStatus.FREE);
		visitEntity.setPatientId(null);
		visitEntity.setUpdatedAt(ZonedDateTime.now());
		visitRepository.save(visitEntity);
		return VisitMapper.visitToDto(visitEntity);
	}


}
