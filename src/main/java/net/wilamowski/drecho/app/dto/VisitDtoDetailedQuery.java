package net.wilamowski.drecho.app.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class VisitDtoDetailedQuery {
  private UserDto selectedRegistrant;
  private UserDto selectedPerformer;
  private LocalDateTime realizationDateTime;
  private LocalDateTime viewStartDateTime;
  private PatientDto patient;

  private VisitDtoDetailedQuery(
      UserDto selectedRegistrant,
      UserDto selectedPerformer,
      LocalDateTime realizationDateTime,
      LocalDateTime viewStartDateTime,
      PatientDto patient) {
    this.selectedRegistrant = selectedRegistrant;
    this.selectedPerformer = selectedPerformer;
    this.realizationDateTime = realizationDateTime;
    this.viewStartDateTime = viewStartDateTime;
    this.patient = patient;
  }
}
