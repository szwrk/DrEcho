package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

import java.time.LocalDateTime;
import lombok.Builder;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;

@Builder
public record Visit (
  Long id,
  User selectedRegistrant,
  User selectedPerformer,
  LocalDateTime realizationDateTime,
  LocalDateTime viewStartDateTime,
  Patient patient){
}
