package net.wilamowski.drecho.standalone.persistance;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.echo.EchoTieUnmodifiable;

public interface EchoTteRepository {
  void save(EchoTieUnmodifiable object);

  Optional<EchoTieUnmodifiable> getById(Long id);

  void addExhamination(EchoTieUnmodifiable echoTteBean);

  List<EchoTieUnmodifiable> getAll(); // todo partitioning
}
