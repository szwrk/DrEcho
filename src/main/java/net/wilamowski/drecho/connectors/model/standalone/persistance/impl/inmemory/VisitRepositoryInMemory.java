package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitEntity;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.demo.DemoDataGeneratorInMemory;
import net.wilamowski.drecho.connectors.properties.BackendPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VisitRepositoryInMemory implements VisitRepository {
  private static final Logger logger = LogManager.getLogger(VisitRepositoryInMemory.class);
  private Set<VisitEntity> visits = new HashSet<>();
  private DemoDataGeneratorInMemory dummyDataGeneratorInMemory;
  private VisitRepositoryInMemory() {
    initializeDemoDataIfPropertyIsEnabled( );
  }

  public static VisitRepositoryInMemory createVisitRepositoryInMemory() {
    return new VisitRepositoryInMemory(  );
  }

  private void initializeDemoDataIfPropertyIsEnabled() {
    if ( Boolean.parseBoolean( BackendPropertyReader.getString( "user.demo.data.initialization.enabled" ) )){
      dummyDataGeneratorInMemory = DemoDataGeneratorInMemory.instance();
      visits = dummyDataGeneratorInMemory.loadDemoVisits( );
    }
  }

  @Override
  public Set<VisitEntity> findAll(int page, int pageSize) {
    Object ignoreFilterAndReturnAll;
    Set<VisitEntity> visitEntities = ignoreFilterAndReturnAll();
    logger.debug("[REPOSITORY] - Visit repository returns {} items", visitEntities.size());
    return visitEntities;
  }

  @Override
  public Set<VisitEntity> findVisitsByPatientId(long patientId) {
    Set<VisitEntity> visitEntities =
        visits.stream()
            .filter(patient -> patient.getPatientId().equals(patientId))
            .collect(Collectors.toSet());
    logger.debug("[REPOSITORY] - Visit repository returns {} items", visitEntities.size());
    return visitEntities;
  }

  @Override
  public Set<VisitEntity> findVisitByDate(LocalDate date) {
    Set<VisitEntity> visitEntities =
        visits.stream()
            .filter(patient -> patient.getRealizationDateTimeProperty().toLocalDate().equals(date))
            .collect(Collectors.toSet());
    logger.debug("[REPOSITORY] - Visit repository returns {} items", visitEntities.size());
    return visitEntities;
  }

  private Set<VisitEntity> ignoreFilterAndReturnAll() {
    logger.warn("[REPOSITORY] Visit repository - ignoring filter is on!");
    return visits;
  }
}
