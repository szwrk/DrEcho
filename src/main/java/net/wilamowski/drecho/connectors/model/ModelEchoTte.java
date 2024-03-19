package net.wilamowski.drecho.connectors.model;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.echotte.EchoTteUnmodifable;

public interface ModelEchoTte {
  void addExhamination(EchoTteUnmodifable echoTteBean);

  Optional<EchoTteUnmodifable> findById(Long id);

  List<EchoTteUnmodifable> findAll();
}
