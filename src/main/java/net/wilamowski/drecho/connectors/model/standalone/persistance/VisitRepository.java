package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.time.LocalDate;
import java.util.Set;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitEntity;

public interface VisitRepository {
  Set<VisitEntity> findAll(int page, int pageSize);

  Set<VisitEntity> findVisitsByPatientId(long patientId);

  Set<VisitEntity> findVisitByDate(LocalDate date);
}
