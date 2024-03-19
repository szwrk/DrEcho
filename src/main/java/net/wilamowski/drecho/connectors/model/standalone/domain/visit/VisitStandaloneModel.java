package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.VisitModel;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.connectors.model.servicemapper.VisitAssembler;
import net.wilamowski.drecho.connectors.infrastructure.VisitDto;
import net.wilamowski.drecho.connectors.model.standalone.persistance.mapper.VisitEntityMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class VisitStandaloneModel implements VisitModel {
  private static final Logger logger = LogManager.getLogger(VisitStandaloneModel.class);
  private final PatientRepository patientRepository;
  private final VisitRepository visitRepository;
  private final UserRepository userRepository;
  private final VisitEntityMapper visitEntityMapper;

  public VisitStandaloneModel(
      VisitRepository repository,
      UserRepository userRepository,
      PatientRepository patientRepository) {
    this.visitRepository = repository;
    this.userRepository = userRepository;
    this.patientRepository = patientRepository;
    this.visitEntityMapper = new VisitEntityMapper(userRepository, patientRepository);
  }

  @Override
  public Set<VisitDto> listVisitsBy(int page, int pageSize) {
    logger.debug("[SERVICE] Getting data by page and page size...");
    Set<VisitEntity> all = visitRepository.findAll(page, pageSize);
    return mapVisitsToDto(all, page, pageSize);
  }

  @Override
  public Set<VisitDto> listVisitsBy(Patient patient) {
    assert patient != null;
    logger.debug("[SERVICE] Getting data by patient {}...", patient.getId());
    Set<VisitEntity> visitEntities = visitRepository.findVisitsByPatientId(patient.getId());
    return mapVisitsToDto(visitEntities, patient.getId());
  }

  @Override
  public Set<VisitDto> listVisitsBy(LocalDate date) {
    logger.debug("[SERVICE] Getting data by date {}...", date);
    Set<VisitEntity> visitEntities = visitRepository.findVisitByDate(date);
    return mapVisitsToDto(visitEntities, date);
  }

  private Set<VisitDto> mapVisitsToDto(Set<VisitEntity> visitEntities, Object... parameters) {
    Set<Visit> visits = visitEntities.stream().map( visitEntityMapper::toDomain).collect(Collectors.toSet());
    logger.debug(
        "[SERVICE] - Visit Service returns {} items. Parameter: {}", visits.size(), parameters);
    return visits.stream().map(VisitAssembler::toDto).collect(Collectors.toSet());
  }
}
