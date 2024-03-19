package net.wilamowski.drecho.connectors.model.standalone.domain.visit;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;

@ToString
@Getter
@Setter
@Builder
public class Visit {

    private User selectedRegistrant;
    private User selectedPerformer;
    private LocalDateTime realizationDateTime;
    private LocalDateTime viewStartDateTime;
    private Patient patient;

    private Visit(User selectedRegistrant, User selectedPerformer, LocalDateTime realizationDateTime, LocalDateTime viewStartDateTime, Patient patient) {
        this.selectedRegistrant = selectedRegistrant;
        this.selectedPerformer = selectedPerformer;
        this.realizationDateTime = realizationDateTime;
        this.viewStartDateTime = viewStartDateTime;
        this.patient = patient;
    }
}
