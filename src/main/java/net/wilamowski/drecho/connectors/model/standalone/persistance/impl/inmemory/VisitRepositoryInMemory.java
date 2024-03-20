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
  private static VisitRepositoryInMemory instance;
  private Set<VisitEntity> visitsDatabase = new HashSet<>();
  private static DemoDataGeneratorInMemory demoDataGeneratorInMemory;

  private VisitRepositoryInMemory() {
    boolean isEnabled = BackendPropertyReader.getBoolean( "user.demo.random-data.initialization.enabled" );
    initializeDemoDataIfPropertyIsEnabled(isEnabled);
  }

  private void initializeDemoDataIfPropertyIsEnabled(boolean isEnabled) {
    if (isEnabled) {
      logger.info("Demo data initialization is enabled");
      demoDataGeneratorInMemory = DemoDataGeneratorInMemory.instance();
      visitsDatabase = demoDataGeneratorInMemory.loadDemoVisits();
    }
  }

  public static VisitRepositoryInMemory createVisitRepositoryInMemory() {
    if (instance==null){
      logger.debug( "Visit repository is null. Creating new instance..." );
      instance = new VisitRepositoryInMemory();
    }
    return instance ;
  }


  @Override
  public Set<VisitEntity> findVisitsByPatientId(long patientId) {
    Set<VisitEntity> matchingVisits =
        visitsDatabase.stream()
            .filter(patient -> patient.getPatientId().equals(patientId))
            .collect(Collectors.toSet());
    logger.debug("[REPOSITORY] - Visit repository returned {}/{} items", matchingVisits.size(), visitsDatabase.size());
    return matchingVisits;
  }

  @Override
  public Set<VisitEntity> findVisitByDate(LocalDate date) {
    Set<VisitEntity> matchingVisits =
        visitsDatabase.stream()
            .filter(patient -> patient.getRealizationDateTimeProperty().toLocalDate().equals(date))
            .collect(Collectors.toSet());
    logger.debug("[REPOSITORY] - Visit repository returned {}/{} items", matchingVisits.size(), visitsDatabase.size());
    return matchingVisits;
  }

  private Set<VisitEntity> ignoreFilterAndReturnAll() {
    logger.warn("[REPOSITORY] Visit repository - ignoring filter is on!");
    return visitsDatabase;
  }
}
