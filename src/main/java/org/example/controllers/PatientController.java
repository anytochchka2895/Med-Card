package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.PatientDto;
import org.example.services.PatientService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PatientController {
	private final PatientService patientService;

	@PostMapping(value = "/patients")
	public PatientDto savePatient(@RequestBody PatientDto patientDto){
		return patientService.savePatient(patientDto);
	}


}
