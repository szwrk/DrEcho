package net.wilamowski.drecho.client.presentation.patients;

import java.time.LocalDate;
import javafx.beans.property.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
@Getter
@Builder
public class PatientVM {
  private static final Logger logger = LogManager.getLogger( PatientVM.class);
  private LongProperty id;
  private StringProperty name;
  private StringProperty lastName;
  @ToString.Exclude private StringProperty pesel;
  private StringProperty nameOfCityBirth;
  private StringProperty codeOfCityBirth;
  private ObjectProperty<LocalDate> dateBirth;
  private StringProperty generalPatientNote;
  private StringProperty patientTelephoneNumber;

  public static PatientVM createEmptyPatientFx() {
    logger.debug("Creating an empty PatientFx object");
    SimpleLongProperty id = new SimpleLongProperty();
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleStringProperty lastName = new SimpleStringProperty();
    SimpleStringProperty pesel = new SimpleStringProperty();
    SimpleStringProperty nameOfCityBirth = new SimpleStringProperty();
    SimpleStringProperty codeOfCityBirth = new SimpleStringProperty();
    SimpleObjectProperty dateBirth = new SimpleObjectProperty<>();
    SimpleStringProperty generalPatientNote = new SimpleStringProperty();
    SimpleStringProperty patientTelephoneNumber = new SimpleStringProperty();
    return new PatientVM(
        id,
        name,
        lastName,
        pesel,
        nameOfCityBirth,
        codeOfCityBirth,
        dateBirth,
        generalPatientNote,
        patientTelephoneNumber);
  }

}
