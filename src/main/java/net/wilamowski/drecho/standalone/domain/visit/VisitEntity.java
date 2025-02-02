package net.wilamowski.drecho.standalone.domain.visit;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class VisitEntity {
  private String selectedRegistrant;
  private String selectedPerformer;
  private LocalDateTime realizationDateTimeProperty;
  private LocalDateTime viewStartDateTimeProperty;
  private Long patientId;
}
