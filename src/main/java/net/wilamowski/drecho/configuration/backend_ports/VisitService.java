package net.wilamowski.drecho.configuration.backend_ports;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.app.dto.VisitDtoCreate;
import net.wilamowski.drecho.app.dto.VisitDtoDetailedQuery;
import net.wilamowski.drecho.app.dto.VisitDtoResponse;

public interface VisitService {

  Set<VisitDtoDetailedQuery> listVisitsBy(PatientDto patient, int page);

  Set<VisitDtoDetailedQuery> listVisitsBy(LocalDate date, int page);

  Optional<VisitDtoResponse> save(VisitDtoCreate dto);
}
