package net.wilamowski.drecho.standalone.persistance;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import net.wilamowski.drecho.standalone.domain.visit.Visit;

public interface VisitRepository {

  Set<Visit> findVisitsByPatientId(long patientId);

  Set<Visit> findVisitByDate(LocalDate date);

  Optional<Visit> save(Visit visit);
}
