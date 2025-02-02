package net.wilamowski.drecho.standalone.persistance;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.echo.EchoTteUnmodifable;

public interface EchoTteRepository {
  void save(EchoTteUnmodifable object);

  Optional<EchoTteUnmodifable> getById(Long id);

  void addExhamination(EchoTteUnmodifable echoTteBean);

  List<EchoTteUnmodifable> getAll(); // todo partitioning
}
