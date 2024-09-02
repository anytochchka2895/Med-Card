package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.DoctorDto;
import org.example.services.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class DoctorController {
	private final DoctorService doctorService;

	@PostMapping(value = "/doctors")
	public DoctorDto saveDoctor(@RequestBody DoctorDto doctorDto){
		return doctorService.saveDoctor(doctorDto);
	}


	@GetMapping(value = "/doctors")
	public List<DoctorDto> getAllActiveDoctors(){
		return doctorService.getAllActiveDoctors();
	}


	@GetMapping(value = "/doctors/clinic/{clinicId}")
	public List<DoctorDto> getAllActiveDoctorsByClinicId(@PathVariable UUID clinicId){
		return doctorService.getAllActiveDoctorsByClinicId(clinicId);
	}


	@DeleteMapping(value = "/doctors/{doctorId}")
	public DoctorDto deleteDoctor(@PathVariable UUID doctorId){
		return doctorService.deleteDoctor(doctorId);
	}


}
