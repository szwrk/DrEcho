package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
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
  private Set<Visit> visits = new HashSet<>();

  private VisitRepositoryInMemory(DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    VisitRepositoryInMemory.demoDataGeneratorInMemory = demoDataGeneratorInMemory;
    boolean isEnabled =
        BackendPropertyReader.getBoolean("user.demo.random-data.initialization.enabled");
    initializeDemoDataIfPropertyIsEnabled(isEnabled);
  }

  private void initializeDemoDataIfPropertyIsEnabled(boolean isEnabled) {
    if (isEnabled) {
      logger.info("Demo data initialization is enabled");
      visits = demoDataGeneratorInMemory.generateVisitsForPatients();
      logger.debug(
          "[REPOSITORY] Added genereted visits to repository. Repository size: {}",
          visits.size());
    }
  }

  public static VisitRepositoryInMemory createVisitRepositoryInMemory(
      DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    return new VisitRepositoryInMemory(demoDataGeneratorInMemory);
  }

  @Override
  public Set<Visit> findVisitsByPatientId(long patientId) {
    Set<Visit> matchingVisits =
        visits.stream()
            .filter(visit -> visit.patient().getId().equals(patientId))
            .collect(Collectors.toSet());
    logger.debug(
        "[REPOSITORY] - Visit repository returned {}/{} items",
        matchingVisits.size(),
        visits.size());
    return matchingVisits;
  }

  @Override
  public Set<Visit> findVisitByDate(LocalDate date) {
    Set<Visit> matchingVisits =
        visits.stream()
            .filter(patient -> patient.realizationDateTime().toLocalDate().equals(date))
            .collect(Collectors.toSet());
    logger.debug(
        "[REPOSITORY] - Visit repository returned {}/{} items",
        matchingVisits.size(),
        visits.size());
    return matchingVisits;
  }

  @Override
  public Optional<Visit> save(Visit visit) {
    logger.trace( "[REPOSITORY] Entering save visit" );
    boolean add = visits.add( visit );
    if (add){
      logger.trace( "[REPOSITORY] Exiting save visit" );
      return Optional.of( visit );
    } else {
      logger.trace( "[REPOSITORY] Exiting save visit" );
      return Optional.empty();
    }
  }

  private Set<Visit> ignoreFilterAndReturnAll() {
    logger.warn("[REPOSITORY] Visit repository - ignoring filter is on!");
    return visits;
  }
}
