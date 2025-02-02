package net.wilamowski.drecho.infra.connectors.mappers;

import net.wilamowski.drecho.app.dto.*;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import net.wilamowski.drecho.standalone.domain.user.account.User;
import net.wilamowski.drecho.standalone.domain.visit.Visit;

public class VisitDomainDtoMapper {

  public static VisitDtoDetailedQuery toDtoDetailedQuery(Visit visit) {
    UserDto    userRegistrant = UserDomainDtoMapper.toDto(visit.selectedRegistrant());
    UserDto    userPerformer  = UserDomainDtoMapper.toDto(visit.selectedPerformer());
    PatientDto patient        = PatientDomainDtoMapper.toDto(visit.patient());
    return VisitDtoDetailedQuery.builder()
        .selectedRegistrant(userRegistrant)
        .selectedPerformer(userPerformer)
        .realizationDateTime(visit.realizationDateTime())
        .viewStartDateTime(visit.viewStartDateTime())
        .patient(patient)
        .build();
  }

  public static VisitDtoCreate toDtoSave(Visit visit) {
    User selectedPerformer = visit.selectedPerformer( );
    User selectedRegistrant = visit.selectedRegistrant( );
    return VisitDtoCreate.builder()
        .performerLogin(selectedPerformer.getLogin())
        .registrantLogin(selectedRegistrant.getLogin())
        .realizationDateTime(visit.realizationDateTime())
        .viewStartDateTime(visit.viewStartDateTime())
        .patientId(visit.patient().getId())
        .build();
  }

  public static VisitDtoResponse toDtoResponse(Visit visit) {
    User selectedPerformer = visit.selectedPerformer( );
    User selectedRegistrant = visit.selectedRegistrant( );
    Patient patient = visit.patient( );
    return VisitDtoResponse.builder()
            .visitId(visit.id())
            .performerLogin(selectedPerformer.getLogin())
            .registrantLogin(selectedRegistrant.getLogin())
            .realizationDateTime(visit.realizationDateTime())
            .viewStartDateTime(visit.viewStartDateTime())
            .patientId(patient.getId() )
            .build();
  }

  public static Visit toDomain(VisitDtoDetailedQuery detailedVisitDto) {
    User registrant = UserDomainDtoMapper.toDomain(detailedVisitDto.getSelectedRegistrant());
    User performer = UserDomainDtoMapper.toDomain(detailedVisitDto.getSelectedPerformer());
    Patient patient = PatientDomainDtoMapper.toDomain(detailedVisitDto.getPatient());
    return Visit.builder()
        .selectedRegistrant(registrant)
        .selectedPerformer(performer)
        .realizationDateTime(detailedVisitDto.getRealizationDateTime())
        .viewStartDateTime(detailedVisitDto.getViewStartDateTime())
        .patient(patient)
        .build();
  }
}
