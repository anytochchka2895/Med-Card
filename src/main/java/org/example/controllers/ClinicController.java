package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.ClinicDto;
import org.example.services.ClinicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClinicController {
	private final ClinicService clinicService;

	@PostMapping(value = "/clinics")
	public ClinicDto saveClinic(@RequestBody ClinicDto clinicDto){
		return clinicService.saveClinic(clinicDto);
	}


	@GetMapping(value = "/clinics")
	public List<ClinicDto> getAllClinics(){
		return clinicService.getAllClinics();
	}




}
