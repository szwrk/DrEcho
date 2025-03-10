package net.wilamowski.drecho.standalone.domain.visit;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.configuration.backend_ports.VisitService;
import net.wilamowski.drecho.infra.connectors.mappers.VisitDomainDtoMapper;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import net.wilamowski.drecho.standalone.domain.user.UserService;
import net.wilamowski.drecho.standalone.domain.user.account.User;
import net.wilamowski.drecho.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.app.dto.VisitDtoCreate;
import net.wilamowski.drecho.app.dto.VisitDtoDetailedQuery;
import net.wilamowski.drecho.app.dto.VisitDtoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class VisitServiceService implements VisitService {
  private static final Logger logger = LogManager.getLogger( VisitServiceService.class);
  private final VisitRepository visitRepository;
  private final UserService userService;
  private final PatientService patientService;

  public VisitServiceService(
          VisitRepository visit,
      UserService user,
      PatientService patient) {
    this.visitRepository = visit;
    this.userService = user;
    this.patientService = patient;
  }

  @Override
  public Set<VisitDtoDetailedQuery> listVisitsBy(PatientDto patient, int page) {
    assert patient != null;
    logger.debug("[SERVICE] Getting data by patient {}...", patient.getId());
    Set<Visit> visits = visitRepository.findVisitsByPatientId(patient.getId());
    return toDto(visits, patient.getId());
  }

  @Override
  public Set<VisitDtoDetailedQuery> listVisitsBy(LocalDate date, int page) {
    logger.debug("[SERVICE] Getting data by date {}...", date);
    Set<Visit> visits = visitRepository.findVisitByDate(date);
    return toDto(visits, date);
  }

  @Override
  public Optional<VisitDtoResponse> save(VisitDtoCreate dto) {
    logger.trace( "[SERVICE] Entering save" );
    Visit visit = convertFlatDtoToDomainVisit( dto );
    validatePatientExist( visit );
    validatePerformerExist( visit );
    validateSelectedRegistrant( visit );

    Optional<Visit> savedVisitOptional = visitRepository.save( visit );
    if (savedVisitOptional.isPresent()){
      Visit          savedVisit                = savedVisitOptional.get( );
      VisitDtoResponse dtoResponse             = VisitDomainDtoMapper.toDtoResponse( savedVisit );
      logger.trace( "[SERVICE] Exiting save" );
      logger.debug( "[SERVICE] Save complete. Return value: {}", dtoResponse );
      return Optional.of( dtoResponse );
    } else {
      logger.debug( "[SERVICE] Visit is not present. Return empty. " );
      logger.trace( "[SERVICE] Exiting save" );
      return Optional.empty();
    }
  }

  private static void validateSelectedRegistrant(Visit visit) {
    if ( visit.selectedRegistrant()==null){
      logger.error( "Patient cannot be empty!"  );
      throw new IllegalArgumentException( "Patient cannot be empty!" );
    }
  }

  private static void validatePerformerExist(Visit visit) {
    if ( visit.selectedPerformer()==null){
      logger.error( "Patient cannot be empty!"  );
      throw new IllegalArgumentException( "Patient cannot be empty!" );
    }
  }

  private static void validatePatientExist(Visit visit) {
    if ( visit.patient()==null){
      logger.error( "Patient cannot be empty!"  );
      throw new IllegalArgumentException( "Patient cannot be empty!" );
    }
  }

  private Visit convertFlatDtoToDomainVisit(VisitDtoCreate dto) {
    String            performerLogin = dto.performerLogin( );
    String            registrantLogin = dto.registrantLogin( );
    Optional<User>    performerOptional  = userService.findDomainUserByLogin( performerLogin );
    Optional<User>     registrantOptional = userService.findDomainUserByLogin( registrantLogin );
    Optional <Patient> patientOptional    =  patientService.findById( dto.patientId() );

    if (performerOptional.isEmpty() || registrantOptional.isEmpty()){
      logger.error( "[SERVICE] Cannot create visit without perfomer or registrant value" );
      throw new IllegalArgumentException( "[SERVICE] Cannot create visit without perfomer or registrant value" );
    }

    if (patientOptional.isEmpty()){
      logger.error( "[SERVICE] Cannot create visit without perfomer or registrant value" );
      throw new IllegalArgumentException( "[SERVICE] Cannot create visit without patient" );
    }

      return Visit.builder( )
            .selectedPerformer( performerOptional.get( ) )
            .selectedRegistrant( performerOptional.get( ) )
            .realizationDateTime( dto.realizationDateTime( ) )
            .realizationDateTime( dto.realizationDateTime( ) )
            .viewStartDateTime( dto.viewStartDateTime( ) )
            .patient( patientOptional.get( ) )
            .build( );
  }

  private Set<VisitDtoDetailedQuery> toDto(Set<Visit> visits, Object... parameters) {
    logger.debug(
        "[SERVICE] - Visit Service returns {} items. Parameter: {}", visits.size(), parameters);
    return visits.stream().map( VisitDomainDtoMapper::toDtoDetailedQuery ).collect(Collectors.toSet());
  }
}
