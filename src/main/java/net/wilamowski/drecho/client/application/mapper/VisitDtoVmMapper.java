package net.wilamowski.drecho.client.application.mapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVMBuilder;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.UserDto;
import net.wilamowski.drecho.shared.dto.VisitDtoCreate;
import net.wilamowski.drecho.shared.dto.VisitDtoDetailedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class VisitDtoVmMapper {
  private static final Logger logger = LogManager.getLogger( VisitDtoVmMapper.class);

  public static Set<VisitVM> toListToVM(Set<VisitDtoDetailedQuery> visitSet) {
    return visitSet.stream().map(visit -> toVM(visit)).collect(Collectors.toSet());
  }
  public static VisitDtoCreate toDtoCreate(VisitVM visitVM) {
    ObjectProperty<PatientVM> selectedPatient = visitVM.getSelectedPatient( );
    return VisitDtoCreate
            .builder()
            .patientId( selectedPatient.get().getId().get() )
            .registrantLogin( visitVM.getSelectedRegistrant().get().getLogin().get() )
            .performerLogin( visitVM.getSelectedPerformer().get().getLogin().get() )
            .realizationDateTime( visitVM.getRealizationDateTimeProperty().get() )
            .viewStartDateTime( visitVM.getViewStartDateTimeProperty().get() )
            .build();
  }

  public static VisitDtoDetailedQuery toDetailedDto(VisitVM visitVM) {
    ObjectProperty<UserVM> registrantProperty = visitVM.getSelectedRegistrant( );
    ObjectProperty<UserVM> performerProperty  = visitVM.getSelectedPerformer( );

    UserDto selectedRegistrant = UserDtoVmMapper.toDto( registrantProperty.get());
    UserDto    selectedPerformer = UserDtoVmMapper.toDto( performerProperty.get());
    PatientDto patient           = PatientDtoVmMapper.toDto(visitVM.getSelectedPatient().get());

    return VisitDtoDetailedQuery.builder()
            .selectedRegistrant(selectedRegistrant)
            .selectedPerformer(selectedPerformer)
            .realizationDateTime(visitVM.getRealizationDateTimeProperty().get())
            .viewStartDateTime(visitVM.getViewStartDateTimeProperty().get())
            .patient(patient)
            .build();
  }
  public static VisitVM toVM(VisitDtoDetailedQuery visit) {
    if (visit == null) {
      return null;
    }
    logger.debug("Transforming Visit to VisitFx: {}", visit);

    ObjectProperty<UserVM> registrantProperty =
        new SimpleObjectProperty<>( UserDtoVmMapper.toVm(visit.getSelectedRegistrant()));
    ObjectProperty<UserVM> performerProperty =
        new SimpleObjectProperty<>( UserDtoVmMapper.toVm(visit.getSelectedPerformer()));
    ObjectProperty<LocalDateTime> realizationDateTimeProperty =
        new SimpleObjectProperty<>(visit.getRealizationDateTime());
    ObjectProperty<LocalDateTime> startDateTimeProperty =
        new SimpleObjectProperty<>(visit.getViewStartDateTime());
    ObjectProperty<PatientVM> selectedPatientProperty =
        new SimpleObjectProperty<>( PatientDtoVmMapper.toVm(visit.getPatient()));

    logger.debug(
        "Transformed Visit to VisitFx instance with registrant: {}, performer: {}, realization date: {}, realization time: {}, start date: {}, start time: {}, patient: {}",
        registrantProperty.get(),
        performerProperty.get(),
        realizationDateTimeProperty.get(),
        startDateTimeProperty.get(),
        selectedPatientProperty.get());

    return new VisitVMBuilder()
            .setSelectedRegistrant(  registrantProperty)
            .setSelectedPerformer( performerProperty )
            .setRealizationDateTimeProperty(realizationDateTimeProperty)
            .setViewStartDateTimeProperty( startDateTimeProperty )
            .setSelectedPatient( selectedPatientProperty )
            .createVisitVM();
  }
}
