package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.VisitModel;
import net.wilamowski.drecho.connectors.model.mapper.VisitDomainDtoMapper;
import net.wilamowski.drecho.connectors.model.standalone.infra.mapper.VisitEntityMapper;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.VisitDto;
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
  public Set<VisitDto> listVisitsBy(PatientDto patient, int page) {
    assert patient != null;
    logger.debug("[SERVICE] Getting data by patient {}...", patient.getId());
    Set<Visit> visits = visitRepository.findVisitsByPatientId(patient.getId());
    return toDto(visits, patient.getId());
  }

  @Override
  public Set<VisitDto> listVisitsBy(LocalDate date, int page) {
    logger.debug("[SERVICE] Getting data by date {}...", date);
    Set<Visit> visits = visitRepository.findVisitByDate(date);
    return toDto(visits, date);
  }

  @Override
  public void save(VisitDto dto) {
    Visit visit = VisitDomainDtoMapper.toDomain( dto );
    visitRepository.save(visit);
  }

  private Set<VisitDto> toDto(Set<Visit> visits, Object... parameters) {
    logger.debug(
        "[SERVICE] - Visit Service returns {} items. Parameter: {}", visits.size(), parameters);
    return visits.stream().map( VisitDomainDtoMapper::toDto).collect(Collectors.toSet());
  }
}
