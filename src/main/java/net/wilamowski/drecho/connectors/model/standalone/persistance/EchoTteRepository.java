package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteUnmodifable;

public interface EchoTteRepository {
  void save(EchoTteUnmodifable object);

  Optional<EchoTteUnmodifable> getById(Long id);

  void addExhamination(EchoTteUnmodifable echoTteBean);

  List<EchoTteUnmodifable> getAll(); // todo partitioning
}
