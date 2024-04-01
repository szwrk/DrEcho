package net.wilamowski.drecho.connectors.model;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.VisitDtoCreate;
import net.wilamowski.drecho.shared.dto.VisitDtoDetailedQuery;
import net.wilamowski.drecho.shared.dto.VisitDtoResponse;

public interface ConnectorVisit {

  Set<VisitDtoDetailedQuery> listVisitsBy(PatientDto patient, int page);

  Set<VisitDtoDetailedQuery> listVisitsBy(LocalDate date, int page);

  Optional<VisitDtoResponse> save(VisitDtoCreate dto);
}
