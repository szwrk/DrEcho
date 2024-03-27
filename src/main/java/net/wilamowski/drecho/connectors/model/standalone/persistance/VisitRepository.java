package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.time.LocalDate;
import java.util.Set;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;

public interface VisitRepository {

  Set<Visit> findVisitsByPatientId(long patientId);

  Set<Visit> findVisitByDate(LocalDate date);

  void save(Visit visit);
}
