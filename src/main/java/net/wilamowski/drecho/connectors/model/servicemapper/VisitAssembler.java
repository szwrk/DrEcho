package net.wilamowski.drecho.connectors.model.servicemapper;

import net.wilamowski.drecho.connectors.infrastructure.VisitDto;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;

public class VisitAssembler {
        public static VisitDto toDto(Visit visit) {
            return VisitDto.builder()
                    .selectedRegistrant(visit.getSelectedRegistrant())
                    .selectedPerformer(visit.getSelectedPerformer())
                    .realizationDateTime(visit.getRealizationDateTime())
                    .viewStartDateTime(visit.getViewStartDateTime())
                    .patient(visit.getPatient())
                    .build();
        }

        public static Visit toDomain(VisitDto visitDto) {
            return Visit.builder()
                    .selectedRegistrant(visitDto.getSelectedRegistrant())
                    .selectedPerformer(visitDto.getSelectedPerformer())
                    .realizationDateTime(visitDto.getRealizationDateTime())
                    .viewStartDateTime(visitDto.getViewStartDateTime())
                    .patient(visitDto.getPatient())
                    .build();
        }

}
