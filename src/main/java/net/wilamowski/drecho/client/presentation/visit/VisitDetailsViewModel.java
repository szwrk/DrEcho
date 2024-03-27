package net.wilamowski.drecho.client.presentation.visit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.UserDtoVmMapper;
import net.wilamowski.drecho.client.application.mapper.VisitDtoVmMapper;
import net.wilamowski.drecho.client.presentation.dictionaries.general.ListLoader;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.model.SimpleDictionariesService;
import net.wilamowski.drecho.connectors.model.VisitModel;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;
import net.wilamowski.drecho.shared.auth.Session;
import net.wilamowski.drecho.shared.dto.UserDto;
import net.wilamowski.drecho.shared.dto.VisitDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
@Getter
public class VisitDetailsViewModel {
  private static final Logger logger = LogManager.getLogger(VisitDetailsViewModel.class);

  private final UserService userService;
  //Form values - Visit details
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
  private VisitModel service = null;
  private SimpleDictionariesService dictService = null;
  public VisitDetailsViewModel(
      VisitModel service, SimpleDictionariesService dictService, UserService userService) {
    this.service = service;
    this.dictService = dictService;
    this.userService = userService;
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
    selectUserByLogin(userPosition.getCode());
  }

  public void selectUserByLogin(String login) {
    logger.trace("[VM] Enter selectUserByLogin: {}", login);
    try {
      Optional<UserDto> userByLogin = userService.getUserByLogin(login);
      if (userByLogin.isPresent()) {
        UserDto userDto = userByLogin.get();
        logger.debug("[VM] Set performer: {}", userDto.getLogin());
        getSelectedPerformer().set(UserDtoVmMapper.toVm(userDto));
      } else {
        logger.debug("[VM] User not found for login: {}", login);
      }
    } catch (Exception e) {
      logger.error("[VM] Error selecting user by login: {}", e.getMessage());
    }
  }

  void initPerformer(ComboBox perfomerComboBox) {
    PositionFx userPosition =
        getPerformerValues().get().stream()
            .filter(pos -> pos.getCode().equals(Session.instance().getUserLogin()))
            .findFirst()
            .orElseThrow();
    perfomerComboBox.setValue(userPosition);
    selectUserByLogin(userPosition.getCode());
  }

  public void setViewStartTimeProperty(LocalDateTime dateTime) {
    this.viewStartProperty.set(dateTime);
  }

  public void confirmVisit() {
    VisitVM visitVM = new VisitVMBuilder( )
            .setViewStartDateTimeProperty( viewStartProperty )
            .setRealizationDateTimeProperty( realizationDateTimeProperty )
            .setSelectedPatient( selectedPatient )
            .setSelectedRegistrant( selectedRegistrant )
            .setSelectedPerformer( selectedPerformer )
            .createVisitVM( );

    VisitDto dto = VisitDtoVmMapper.toDto( visitVM );
    service.save(dto);
  }
  private ObjectProperty<LocalDateTime> createCombinedDateTimeProperty(
          ObjectProperty<LocalDate> dateProperty, ObjectProperty<LocalTime> timeProperty) {
    return new SimpleObjectProperty<>(LocalDateTime.of(dateProperty.get(), timeProperty.get()));
  }
}
