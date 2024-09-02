package org.example.repositories;

import org.example.entities.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<ClinicEntity, UUID> {
	ClinicEntity findByName(String name);

}
