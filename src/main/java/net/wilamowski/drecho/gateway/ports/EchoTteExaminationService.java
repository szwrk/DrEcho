package net.wilamowski.drecho.gateway.ports;

import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.echo.EchoTteUnmodifable;

public interface EchoTteExaminationService {
  void addExhamination(EchoTteUnmodifable echoTteBean);

  Optional<EchoTteUnmodifable> findById(Long id);

  List<EchoTteUnmodifable> findAll();
}
