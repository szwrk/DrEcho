package net.wilamowski.drecho.connectors.model.mapper;

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.UserDto;
import net.wilamowski.drecho.shared.dto.VisitDto;

public class VisitDomainDtoMapper {
  public static VisitDto toDto(Visit visit) {
    UserDto userRegistrant = UserDomainDtoMapper.toDto( visit.getSelectedRegistrant( ) );
    UserDto userPerformer = UserDomainDtoMapper.toDto( visit.getSelectedPerformer( ) );
    PatientDto patient         = PatientDomainDtoMapper.toDto( visit.getPatient( ) );
    return VisitDto.builder()
        .selectedRegistrant(userRegistrant)
        .selectedPerformer(userPerformer)
        .realizationDateTime(visit.getRealizationDateTime())
        .viewStartDateTime(visit.getViewStartDateTime())
        .patient(patient)
        .build();
  }

  public static Visit toDomain(VisitDto visitDto) {
    User registrant = UserDomainDtoMapper.toDomain( visitDto.getSelectedRegistrant( ) );
    User performer = UserDomainDtoMapper.toDomain( visitDto.getSelectedPerformer( ) );
    Patient patient  = PatientDomainDtoMapper.toDomain( visitDto.getPatient( ));
    return Visit.builder()
        .selectedRegistrant(registrant )
        .selectedPerformer(performer )
        .realizationDateTime( visitDto.getRealizationDateTime())
        .viewStartDateTime( visitDto.getViewStartDateTime())
        .patient(patient)
        .build();
  }
}
