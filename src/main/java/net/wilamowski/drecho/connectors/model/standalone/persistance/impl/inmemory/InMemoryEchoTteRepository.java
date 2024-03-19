package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.wilamowski.drecho.connectors.model.standalone.persistance.EchoTteRepository;
import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteUnmodifable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*In-memory implementation*/
public class InMemoryEchoTteRepository implements EchoTteRepository {
  private static final Logger log = LogManager.getLogger( InMemoryEchoTteRepository.class);
  private final List<EchoTteUnmodifable> docs = new ArrayList<>();

  private InMemoryEchoTteRepository() {}

  public static InMemoryEchoTteRepository createRepositoryInMemoryEchoTte() {
    log.traceEntry();
    return new InMemoryEchoTteRepository();
  }

  @Override
  public void save(EchoTteUnmodifable doc) {
    docs.add(doc);
    log.info("[REPOSITORY] Document saved with ID: {}", doc.getId());
    log.debug("[REPOSITORY] Document saved successfully: {}", doc);
  }

  @Override
  public Optional<EchoTteUnmodifable> getById(Long id) {
    EchoTteUnmodifable echoTteBean =
        this.docs.stream().filter(x -> x.getId().equals(id)).findAny().orElseThrow();
    log.debug("[REPOSITORY] Fetched object by ID: id={}, object={}", id, echoTteBean);
    return Optional.of(echoTteBean);
  }

  @Override
  public void addExhamination(EchoTteUnmodifable echoTteBean) {}

  @Override
  public List<EchoTteUnmodifable> getAll() {
    return null;
  }
}
