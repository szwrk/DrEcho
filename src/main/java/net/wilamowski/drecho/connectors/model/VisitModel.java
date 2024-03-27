package net.wilamowski.drecho.connectors.model;

import java.time.LocalDate;
import java.util.Set;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.VisitDto;

public interface VisitModel {

  Set<VisitDto> listVisitsBy(PatientDto patient, int page);

  Set<VisitDto> listVisitsBy(LocalDate date, int page);

  void save(VisitDto dto);
}
