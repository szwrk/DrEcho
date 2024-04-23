package net.wilamowski.drecho.client.presentation.visit;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.exceptions.VisitVmNoPatientSelected;
import net.wilamowski.drecho.client.application.exceptions.VisitVmValidationException;
import net.wilamowski.drecho.client.application.mapper.UserDtoVmMapper;
import net.wilamowski.drecho.client.application.mapper.VisitDtoVmMapper;
import net.wilamowski.drecho.client.presentation.dictionaries.general.ListLoader;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.model.ConnectorSimpleDictionaries;
import net.wilamowski.drecho.connectors.model.ConnectorVisit;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.ConnectorUser;
import net.wilamowski.drecho.shared.auth.Session;
import net.wilamowski.drecho.shared.dto.UserDto;
import net.wilamowski.drecho.shared.dto.VisitDtoCreate;
import net.wilamowski.drecho.shared.dto.VisitDtoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
@Getter
public class VisitViewModel {
  private static final Logger logger = LogManager.getLogger( VisitViewModel.class);

  private final ConnectorUser connectorUser;
  // Form values - Visit details

  private final SimpleLongProperty visitIdProperty = new SimpleLongProperty();
  private final SimpleStringProperty statusProperty = new SimpleStringProperty();
  private final ObjectProperty<PatientVM> selectedPatient = new SimpleObjectProperty<>();
  private final ObjectProperty<UserVM> selectedRegistrant = new SimpleObjectProperty<>();
  private final ObjectProperty<UserVM> selectedPerformer = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> viewStartProperty = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> realizationDateTimeProperty = new SimpleObjectProperty<>();
  //List of values
  private final ListProperty<PositionFx> realizationHoursValues =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ListProperty<PositionFx> realizationMinutesValues =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ListProperty<PositionFx> registrantValues =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ListProperty<PositionFx> performerValues =
      new SimpleListProperty<>(FXCollections.observableArrayList());

  private final ResourceBundle bundle = null;
  private ConnectorVisit service = null;
  private ConnectorSimpleDictionaries dictService = null;
  public VisitViewModel(
          ConnectorVisit service, ConnectorSimpleDictionaries dictService, ConnectorUser connectorUser) {
    this.service = service;
    this.dictService = dictService;
    this.connectorUser = connectorUser;
    initStartDateWithNow();
    initStartTimeWithNow();
    loadDict();
    ListLoader.source(dictService).loadValuesToFxList("VST_REALIZ_HOURS", realizationHoursValues);
    ListLoader.source(dictService).loadValuesToFxList("VST_REALIZ_MIN", realizationMinutesValues);
  }


  private void initStartTimeWithNow() {
    Boolean initVisitRealizationTime =
        Boolean.valueOf(
            ClientPropertyReader.getString("user.ui.quick-visit-view.initialize-realization-time"));
    if (initVisitRealizationTime) {
      viewStartProperty.set(LocalDateTime.now());
    }
  }

  private void initStartDateWithNow() {
    Boolean initVisitRealizationTime =
        Boolean.valueOf(
            ClientPropertyReader.getString("user.ui.quick-visit-view.initialize-realization-date"));
    if (initVisitRealizationTime) {
      viewStartProperty.set(LocalDateTime.now());
    }
  }

  private void loadDict() {
    ListLoader.source(dictService).loadValuesToFxList("PRSPERF", performerValues);
    ListLoader.source(dictService).loadValuesToFxList("PRSREGI", registrantValues);
  }

  void initRegistrant(ComboBox registantCombBox) {
    PositionFx userPosition =
        getPerformerValues().get().stream()
            .filter(pos -> pos.getCode().equals(Session.instance().getUserLogin()))
            .findFirst()
            .orElseThrow(
                () -> {
                  IllegalStateException illegalStateException =
                      new IllegalStateException(
                          "No matching position found for the current user. Check if the value exists in the dictionary, and consider adding it!");
                  logger.error(illegalStateException);
                  throw illegalStateException;
                });
    registantCombBox.setValue(userPosition);
    UserVM registrantByDictCode = findUserByLogin( userPosition.getCode( ) );
    chooseRegistrant( registrantByDictCode );
  }

  public UserVM findUserByLogin(String login) {
    logger.trace("[VM] Enter selectUserByLogin: {}", login);
      Optional<UserDto> userByLogin = connectorUser.findUserByLogin(login);
        UserDto userDto = userByLogin.get();
        logger.debug("[VM] Set performer: {}", userDto.login());
        return UserDtoVmMapper.toVm(userDto);
  }

public void choosePerormer(UserVM user){
  getSelectedPerformer().set(user);
}

  public void chooseRegistrant(UserVM user){
    getSelectedRegistrant().set(user);
  }

  void initPerformer(ComboBox perfomerComboBox) {
    PositionFx userPosition =
        getPerformerValues().get().stream()
            .filter(pos -> pos.getCode().equals(Session.instance().getUserLogin()))
            .findFirst()
            .orElseThrow();
    perfomerComboBox.setValue(userPosition);
    UserVM userByLogin = findUserByLogin( userPosition.getCode( ) );
    choosePerormer( userByLogin );
  }

  public void setViewStartTimeProperty(LocalDateTime dateTime) {
    this.viewStartProperty.set(dateTime);
  }

  public Optional<VisitDtoResponse> confirmVisit() throws VisitVmValidationException {
    logger.debug("[VM] Confirming...");
    if (selectedPatientVm() == null) {
      logger.error("[VM] Patient cannot be null! Use must choose patient.");
      throw new VisitVmNoPatientSelected();
    }
    if (selectedPerformer.get()==null || selectedRegistrant.get()==null){
      logger.error( "[VM] Performer or registrant cannot be null!");
      throw new VisitVmValidationException("e.016.header","e.016.msg");
    }
    if ( realizationDateTimeProperty.get() == null ){
      logger.warn( "[VM] Realization datetime is can not be null!");
      throw new VisitVmValidationException("e.017.header","e.017.msg");
    }

    logger.debug( "[VM] Selected patient get {}", selectedPatientVm( ).getId() );
    VisitVM copyOfVisit = new VisitVMBuilder()
            .setSelectedPatient( selectedPatient )
            .setSelectedPerformer( selectedPerformer )
            .setSelectedRegistrant( selectedRegistrant )
            .setViewStartDateTimeProperty( viewStartProperty )
            .setRealizationDateTimeProperty( realizationDateTimeProperty )
            .createVisitVM();
    Optional<VisitDtoResponse> temp = Optional.empty();

    try {
      VisitDtoCreate dtoCreate = VisitDtoVmMapper.toDtoCreate( copyOfVisit );
      logger.debug("[VM] Visit: {}", dtoCreate);
      temp = service.save( dtoCreate );
      Long newVisitId = temp.get( ).visitId( );
      visitIdProperty.set( newVisitId );
    } catch (RuntimeException e){
      logger.error( e.getMessage(),e );
    }
    return temp;
  }
  public PatientVM selectedPatientVm() {
    return selectedPatient.get( );
  }
  public void selectPatient(PatientVM selectedPatient) {
    this.selectedPatient.set( selectedPatient );
  }

  public void updateVisitStatus() {
      statusProperty.set( "SAVED" );
  }
}
