package org.example.repositories;

import org.example.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {
	DoctorEntity findByFullName(String fullName);

	List<DoctorEntity> findAllByClinicIdAndDeletedIsFalse(UUID clinicId);

	List<DoctorEntity> findAllByDeletedIsFalse();
}
