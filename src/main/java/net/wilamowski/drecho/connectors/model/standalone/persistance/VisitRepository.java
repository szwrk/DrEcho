package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitEntity;

import java.time.LocalDate;
import java.util.Set;

public interface VisitRepository {
  Set<VisitEntity> findAll(int page, int pageSize);

  Set<VisitEntity> findVisitsByPatientId(long patientId);

  Set<VisitEntity> findVisitByDate(LocalDate date);
}
