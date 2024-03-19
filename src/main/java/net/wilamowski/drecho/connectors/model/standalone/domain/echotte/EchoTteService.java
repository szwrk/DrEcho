package net.wilamowski.drecho.connectors.model.standalone.domain.echotte;

import java.util.List;
import java.util.Optional;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.ModelEchoTte;
import net.wilamowski.drecho.connectors.model.standalone.persistance.EchoTteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class EchoTteService implements ModelEchoTte {
  private static final Logger logger = LogManager.getLogger(EchoTteService.class);

  private final EchoTteRepository repository;

  public EchoTteService(EchoTteRepository repository) {
    logger.debug(
        "[SERVICE] Initializing EchoTteService {} ...", repository.getClass().getSimpleName());
    this.repository = repository;
  }

  @Override
  public void addExhamination(EchoTteUnmodifable echoTteBean) {
    logger.debug("[SERVICE] Saving data...");
    repository.addExhamination(echoTteBean);
  }

  @Override
  public Optional<EchoTteUnmodifable> findById(Long id) {
    logger.debug("[SERVICE] Finding data by ID...");
    return repository.getById(id);
  }

  @Override
  public List<EchoTteUnmodifable> findAll() { // todo partitioning
    logger.debug("[SERVICE] Finding all...");
    return repository.getAll();
  }
}
