package net.wilamowski.drecho.client.application.mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import net.wilamowski.drecho.connectors.infrastructure.VisitDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */

public class VisitVmMapper {
  private static final Logger logger = LogManager.getLogger( VisitVmMapper.class);

  public static Set<VisitVM> toListToVM(Set<VisitDto> visitSet) {
    return visitSet.stream().map(visit -> toVM(visit)).collect(Collectors.toSet());
  }

  public static VisitVM toVM(VisitDto visit) {
    if (visit == null) {
      return null;
    }
    logger.debug("Transforming Visit to VisitFx: {}", visit);

    ObjectProperty<UserVM> registrantProperty =
        new SimpleObjectProperty<>( UserVmMapper.of(visit.getSelectedRegistrant()));
    ObjectProperty<UserVM> performerProperty =
        new SimpleObjectProperty<>( UserVmMapper.of(visit.getSelectedPerformer()));
    ObjectProperty<LocalDate> realizationDateProperty =
        new SimpleObjectProperty<>(visit.getRealizationDateTime().toLocalDate());
    ObjectProperty<LocalTime> realizationTimeProperty =
        new SimpleObjectProperty<>(visit.getRealizationDateTime().toLocalTime());
    ObjectProperty<LocalDate> startDateProperty =
        new SimpleObjectProperty<>(visit.getViewStartDateTime().toLocalDate());
    ObjectProperty<LocalTime> startTimeProperty =
        new SimpleObjectProperty<>(visit.getViewStartDateTime().toLocalTime());
    ObjectProperty<PatientFx> selectedPatientProperty =
        new SimpleObjectProperty<>( PatientVmMapper.toFx(visit.getPatient()));

    logger.debug(
        "Transformed Visit to VisitFx instance with registrant: {}, performer: {}, realization date: {}, realization time: {}, start date: {}, start time: {}, patient: {}",
        registrantProperty.get(),
        performerProperty.get(),
        realizationDateProperty.get(),
        realizationTimeProperty.get(),
        startDateProperty.get(),
        startTimeProperty.get(),
        selectedPatientProperty.get());

    return VisitVM.createVisitFx(
        registrantProperty,
        performerProperty,
        realizationDateProperty,
        realizationTimeProperty,
        startDateProperty,
        startTimeProperty,
        selectedPatientProperty);
  }
}
