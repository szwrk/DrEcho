package net.wilamowski.drecho.configuration.backend_ports;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.echo.EchoTieUnmodifiable;

public interface EchoTteExaminationService {
  void addExhamination(EchoTieUnmodifiable echoTteBean);

  Optional<EchoTieUnmodifiable> findById(Long id);

  List<EchoTieUnmodifiable> findAll();
}
