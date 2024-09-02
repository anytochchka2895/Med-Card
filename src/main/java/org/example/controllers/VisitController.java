package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.*;
import org.example.enums.VisitStatus;
import org.example.services.VisitService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class VisitController {
	private final VisitService visitService;

	@PostMapping(value = "/visits")
	public List<VisitDto> createVisitsForDoctor(@RequestBody CreateVisitDto createVisitDto) {
		return visitService.createVisitsForDoctor(createVisitDto);
	}


	@GetMapping(value = "/visits/doctors/{doctorId}")
	public List<VisitDto> getAllVisitsForDoctorBetweenDates(@PathVariable UUID doctorId,
	                                                        @RequestParam LocalDate startDate,
	                                                        @RequestParam LocalDate finishDate) {
		return visitService.getAllVisitsForDoctorBetweenDates(doctorId, startDate, finishDate);
	}


	@GetMapping(value = "/visits/patient/{patientId}/by-status")
	public List<VisitPatientDoctorClinicDto> getAllVisitsByStatus(@PathVariable UUID patientId,
	                                                              @RequestParam VisitStatus status) {
		return visitService.getAllVisitsByStatus(patientId, status);
	}


	@PostMapping(value = "/visits/book")
	public VisitDto bookVisit(@RequestBody BookVisitDto bookVisitDto) {
		return visitService.bookVisit(bookVisitDto);
	}


	@PostMapping(value = "/visits/move")
	public VisitDto moveVisit(@RequestBody MoveVisitDto moveVisitDto) {
		return visitService.moveVisit(moveVisitDto);
	}


	@DeleteMapping(value = "/visits/delete")
	public VisitDto deleteVisit(@RequestBody DeleteVisitDto deleteVisitDto) {
		return visitService.deleteVisit(deleteVisitDto);
	}


}
