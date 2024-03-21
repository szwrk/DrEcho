package net.wilamowski.drecho.connectors.model;

import java.time.LocalDate;
import java.util.Set;
import net.wilamowski.drecho.connectors.infrastructure.VisitDto;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;

public interface VisitModel {

  Set<VisitDto> listVisitsBy(Patient patient, int page);

  Set<VisitDto> listVisitsBy(LocalDate date, int page);
}
