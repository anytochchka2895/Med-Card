package org.example.repositories;

import org.example.entities.VisitEntity;
import org.example.enums.VisitStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, UUID> {

	@Modifying
	@Query(value = "delete from VisitEntity v " +
			" where v.doctorId = :doctorId and v.status in :visitStatusList ")
	void deleteFutureVisitsWithDoctorId(UUID doctorId,
	                                    List<VisitStatus> visitStatusList);

	List<VisitEntity> findAllByDoctorIdAndDateBetween(UUID doctorId, LocalDate date1, LocalDate date2);

	@Query(value = " select v from VisitEntity v " +
			" where v.patientId = :patientId and v.status = :status ")
	List<VisitEntity> findAllByPatientIdAndStatus(UUID patientId, VisitStatus status);

}
