package net.wilamowski.drecho.standalone.persistance.impl.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.echo.EchoTieUnmodifiable;
import net.wilamowski.drecho.standalone.persistance.EchoTteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*In-memory implementation*/
public class InMemoryEchoTteRepository implements EchoTteRepository {
  private static final Logger log = LogManager.getLogger(InMemoryEchoTteRepository.class);
  private final List<EchoTieUnmodifiable> docs = new ArrayList<>();

  private InMemoryEchoTteRepository() {}

  public static InMemoryEchoTteRepository createRepositoryInMemoryEchoTte() {
    log.traceEntry();
    return new InMemoryEchoTteRepository();
  }

  @Override
  public void save(EchoTieUnmodifiable doc) {
    docs.add(doc);
    log.info("[REPOSITORY] Document saved with ID: {}", doc.getId());
    log.debug("[REPOSITORY] Document saved successfully: {}", doc);
  }

  @Override
  public Optional<EchoTieUnmodifiable> getById(Long id) {
    EchoTieUnmodifiable echoTteBean =
        this.docs.stream().filter(x -> x.getId().equals(id)).findAny().orElseThrow();
    log.debug("[REPOSITORY] Fetched object by ID: id={}, object={}", id, echoTteBean);
    return Optional.of(echoTteBean);
  }

  @Override
  public void addExhamination(EchoTieUnmodifiable echoTteBean) {}

  @Override
  public List<EchoTieUnmodifiable> getAll() {
    return null;
  }
}
