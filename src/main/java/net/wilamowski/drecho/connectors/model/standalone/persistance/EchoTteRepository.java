package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteUnmodifable;

import java.util.List;
import java.util.Optional;

public interface EchoTteRepository {
  void save(EchoTteUnmodifable object);

  Optional<EchoTteUnmodifable> getById(Long id);

  void addExhamination(EchoTteUnmodifable echoTteBean);

  List<EchoTteUnmodifable> getAll(); // todo partitioning
}
