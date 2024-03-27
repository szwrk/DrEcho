package net.wilamowski.drecho.client.presentation.visit;

import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
@Getter
public class VisitVM {
  private static final Logger logger = LogManager.getLogger(VisitVM.class);
  private final ObjectProperty<LocalDateTime> realizationDateTimeProperty;
  private final ObjectProperty<LocalDateTime> viewStartDateTimeProperty;
  private ObjectProperty<UserVM> selectedRegistrant = new SimpleObjectProperty<>();
  private ObjectProperty<UserVM> selectedPerformer = new SimpleObjectProperty<>();
  private ObjectProperty<PatientVM> selectedPatient = new SimpleObjectProperty<>();

  VisitVM(ObjectProperty<LocalDateTime> realizationDateTimeProperty , ObjectProperty<LocalDateTime> viewStartDateTimeProperty , ObjectProperty<UserVM> selectedRegistrant , ObjectProperty<UserVM> selectedPerformer , ObjectProperty<PatientVM> selectedPatient) {
    this.realizationDateTimeProperty = realizationDateTimeProperty;
    this.viewStartDateTimeProperty = viewStartDateTimeProperty;
    this.selectedRegistrant = selectedRegistrant;
    this.selectedPerformer = selectedPerformer;
    this.selectedPatient = selectedPatient;
  }

}
