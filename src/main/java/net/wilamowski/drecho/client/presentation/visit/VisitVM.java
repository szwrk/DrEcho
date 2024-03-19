package net.wilamowski.drecho.client.presentation.visit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientVmMapper;
import net.wilamowski.drecho.client.application.mapper.UserVmMapper;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;
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
  private ObjectProperty<PatientFx> selectedPatient = new SimpleObjectProperty<>();

  private VisitVM(
      ObjectProperty<UserVM> selectedRegistrant,
      ObjectProperty<UserVM> selectedPerformer,
      ObjectProperty<LocalDate> realizationDtProperty,
      ObjectProperty<LocalTime> realizationTimePropert,
      ObjectProperty<LocalDate> startDtProperty,
      ObjectProperty<LocalTime> startTimeProperty,
      ObjectProperty<PatientFx> selectedPatient) {
    this.selectedRegistrant = selectedRegistrant;
    this.selectedPerformer = selectedPerformer;
    this.realizationDateTimeProperty =
        createCombinedDateTimeProperty(realizationDtProperty, realizationTimePropert);
    this.viewStartDateTimeProperty =
        createCombinedDateTimeProperty(startDtProperty, startTimeProperty);
    this.selectedPatient = selectedPatient;
  }

  private ObjectProperty<LocalDateTime> createCombinedDateTimeProperty(
      ObjectProperty<LocalDate> dateProperty, ObjectProperty<LocalTime> timeProperty) {
    return new SimpleObjectProperty<>(LocalDateTime.of(dateProperty.get(), timeProperty.get()));
  }

  public static VisitVM createVisitFx(
      ObjectProperty<UserVM> selectedRegistrant,
      ObjectProperty<UserVM> selectedPerformer,
      ObjectProperty<LocalDate> realizationDtProperty,
      ObjectProperty<LocalTime> realizationTimePropert,
      ObjectProperty<LocalDate> startDtProperty,
      ObjectProperty<LocalTime> startTimeProperty,
      ObjectProperty<PatientFx> selectedPatient) {

    Objects.requireNonNull(selectedRegistrant, "Field registrant can not be null");
    Objects.requireNonNull(selectedPerformer, "Field performer can not be null");
    Objects.requireNonNull(realizationDtProperty, "Field realization date can not be null");
    Objects.requireNonNull(realizationTimePropert, "Field realization time can not be null");
    Objects.requireNonNull(startDtProperty, "Field visit start date can not be null");
    Objects.requireNonNull(startTimeProperty, "Field visit start time can not be null");
    Objects.requireNonNull(selectedPatient, "Field patient can not be null");
    logger.debug(
        "Creating VisitFx instance with registrant: {}, performer: {}, realization date: {}, realization time: {}, start date: {}, start time: {}, patient: {}",
        selectedRegistrant,
        selectedPerformer,
        realizationDtProperty,
        realizationTimePropert,
        startDtProperty,
        startTimeProperty,
        selectedPatient);

    return new VisitVM(
        selectedRegistrant,
        selectedPerformer,
        realizationDtProperty,
        realizationTimePropert,
        startDtProperty,
        startTimeProperty,
        selectedPatient);
  }

  public static VisitVM toFx(Visit visit) {
    if (visit == null) {
      return null;
    }
    logger.debug("Transforming Visit to VisitFx: {}", visit);

    ObjectProperty<UserVM> registrantProperty =
        new SimpleObjectProperty<>(UserVmMapper.of(visit.getSelectedRegistrant()));
    ObjectProperty<UserVM> performerProperty =
        new SimpleObjectProperty<>(UserVmMapper.of(visit.getSelectedPerformer()));
    ObjectProperty<LocalDate> realizationDateProperty =
        new SimpleObjectProperty<>(visit.getRealizationDateTime().toLocalDate());
    ObjectProperty<LocalTime> realizationTimeProperty =
        new SimpleObjectProperty<>(visit.getRealizationDateTime().toLocalTime());
    ObjectProperty<LocalDate> startDateProperty =
        new SimpleObjectProperty<>(visit.getViewStartDateTime().toLocalDate());
    ObjectProperty<LocalTime> startTimeProperty =
        new SimpleObjectProperty<>(visit.getViewStartDateTime().toLocalTime());
    ObjectProperty<PatientFx> selectedPatientProperty =
        new SimpleObjectProperty<>(PatientVmMapper.toFx(visit.getPatient()));

    logger.debug(
        "Transformed Visit to VisitFx instance with registrant: {}, performer: {}, realization date: {}, realization time: {}, start date: {}, start time: {}, patient: {}",
        registrantProperty.get(),
        performerProperty.get(),
        realizationDateProperty.get(),
        realizationTimeProperty.get(),
        startDateProperty.get(),
        startTimeProperty.get(),
        selectedPatientProperty.get());

    return new VisitVM(
        registrantProperty,
        performerProperty,
        realizationDateProperty,
        realizationTimeProperty,
        startDateProperty,
        startTimeProperty,
        selectedPatientProperty);
  }
}
