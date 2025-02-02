package net.wilamowski.drecho.standalone.domain.echo;

import java.util.List;
import java.util.Optional;
import lombok.ToString;
import net.wilamowski.drecho.gateway.ports.EchoTteExaminationService;
import net.wilamowski.drecho.standalone.persistance.EchoTteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class EchoTteServiceExaminationService implements EchoTteExaminationService {
  private static final Logger logger = LogManager.getLogger( EchoTteServiceExaminationService.class);

  private final EchoTteRepository repository;

  public EchoTteServiceExaminationService(EchoTteRepository repository) {
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
