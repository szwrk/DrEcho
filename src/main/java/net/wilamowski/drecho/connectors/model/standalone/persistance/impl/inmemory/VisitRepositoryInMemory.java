package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VisitRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.demo.DemoDataGeneratorInMemory;
import net.wilamowski.drecho.connectors.properties.BackendPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VisitRepositoryInMemory implements VisitRepository {
  private static final Logger logger = LogManager.getLogger(VisitRepositoryInMemory.class);
  private static VisitRepositoryInMemory instance;
  private static DemoDataGeneratorInMemory demoDataGeneratorInMemory;
  private Set<Visit> visitsDatabase = new HashSet<>();

  private VisitRepositoryInMemory(DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    VisitRepositoryInMemory.demoDataGeneratorInMemory = demoDataGeneratorInMemory;
    boolean isEnabled =
        BackendPropertyReader.getBoolean("user.demo.random-data.initialization.enabled");
    initializeDemoDataIfPropertyIsEnabled(isEnabled);
  }

  private void initializeDemoDataIfPropertyIsEnabled(boolean isEnabled) {
    if (isEnabled) {
      logger.info("Demo data initialization is enabled");
      visitsDatabase = demoDataGeneratorInMemory.generateVisitsForPatients();
      logger.debug(
          "[REPOSITORY] Added genereted visits to repository. Repository size: {}",
          visitsDatabase.size());
    }
  }

  public static VisitRepositoryInMemory createVisitRepositoryInMemory(
      DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    return new VisitRepositoryInMemory(demoDataGeneratorInMemory);
  }

  @Override
  public Set<Visit> findVisitsByPatientId(long patientId) {
    Set<Visit> matchingVisits =
        visitsDatabase.stream()
            .filter(patient -> patient.getPatient().getId().equals(patientId))
            .collect(Collectors.toSet());
    logger.debug(
        "[REPOSITORY] - Visit repository returned {}/{} items",
        matchingVisits.size(),
        visitsDatabase.size());
    return matchingVisits;
  }

  @Override
  public Set<Visit> findVisitByDate(LocalDate date) {
    Set<Visit> matchingVisits =
        visitsDatabase.stream()
            .filter(patient -> patient.getRealizationDateTime().toLocalDate().equals(date))
            .collect(Collectors.toSet());
    logger.debug(
        "[REPOSITORY] - Visit repository returned {}/{} items",
        matchingVisits.size(),
        visitsDatabase.size());
    return matchingVisits;
  }

  @Override
  public void save(Visit visit) {
    visitsDatabase.add( visit );
  }

  private Set<Visit> ignoreFilterAndReturnAll() {
    logger.warn("[REPOSITORY] Visit repository - ignoring filter is on!");
    return visitsDatabase;
  }
}
