package net.wilamowski.drecho.connectors.infrastructure;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder
public class VisitDto {
  private User selectedRegistrant;
  private User selectedPerformer;
  private LocalDateTime realizationDateTime;
  private LocalDateTime viewStartDateTime;
  private Patient patient;

  private VisitDto(
      User selectedRegistrant,
      User selectedPerformer,
      LocalDateTime realizationDateTime,
      LocalDateTime viewStartDateTime,
      Patient patient) {
    this.selectedRegistrant = selectedRegistrant;
    this.selectedPerformer = selectedPerformer;
    this.realizationDateTime = realizationDateTime;
    this.viewStartDateTime = viewStartDateTime;
    this.patient = patient;
  }
}
