package net.wilamowski.drecho.connectors.model;

import java.time.LocalDate;
import java.util.Set;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.infrastructure.VisitDto;

public interface VisitModel {

    Set<VisitDto> listVisitsBy(int page, int pageSize);

    Set<VisitDto> listVisitsBy(Patient patient);

    Set<VisitDto> listVisitsBy(LocalDate date);
}
