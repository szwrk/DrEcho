package net.wilamowski.drecho.client.presentation.visit;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.dictionaries.general.DictionaryConverter;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class VisitController implements Initializable, ViewModelsInitializer, PostInitializable {
  private static final Logger logger = LogManager.getLogger(VisitController.class);

  @FXML private ComboBox<PositionFx> performerComboBox;

  @FXML private ComboBox<PositionFx> registrantComboBox;

  @FXML private DatePicker realizationDatePicker;

  @FXML private ChoiceBox<PositionFx> realizationHourChoiceBox;
  @FXML private ChoiceBox<PositionFx> realizationTimeChoiceBox;

  @FXML private DatePicker saveDatePicker;
  @FXML private Label idLabel;
  @FXML private Label statusLabel;

  @FXML private TitledPane visitVBox;
  private ResourceBundle bundle;
  private VisitViewModel visitViewModel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
  }

  @Override
  public void initializeViewModels(ViewModelConfiguration viewModelConfigurationFactory) {
    logger.debug("VisitController init Visit VM...");
    this.visitViewModel = viewModelConfigurationFactory.visitViewModel();
  }

  @Override
  public void postInitialize() {
    realizationDatePicker.setValue(LocalDate.now());
    updateRealizationDateTimeListener(realizationDatePicker.valueProperty());
    updateRealizationDateTimeListener(realizationHourChoiceBox.valueProperty());
    updateRealizationDateTimeListener(realizationTimeChoiceBox.valueProperty());

    Platform.runLater(
        () -> {
          bindPerformer();
          bindRegistrant();
        });

    Platform.runLater(
        () -> {
          initializeRegistrant();
          initializePerformer();
        });

    configureHourChoiceBox();
    configureMinutesChoiceBox();
      bindVisitId( );
      bindStatus( );
  }

    private void bindStatus() {
        statusLabel.textProperty().bind(visitViewModel.getStatusProperty());
    }

    private void bindVisitId() {
        idLabel
            .textProperty()
            .bind(
                Bindings.createStringBinding(
                    () -> String.valueOf(visitViewModel.getVisitIdProperty().get()),
                    visitViewModel.getVisitIdProperty()));
    }

    private void updateRealizationDateTimeListener(ObjectProperty property) {
    property.addListener(
        (obs, old, newVal) -> {
          LocalDate date = realizationDatePicker.getValue();
          if (date != null
              && realizationHourChoiceBox.getValue() != null
              && realizationTimeChoiceBox.getValue() != null) {
            LocalDateTime localDateTime = createDateTime();
            logger.debug("[CONTROLLER] Updated realization date and time: {}", localDateTime);
            visitViewModel.getRealizationDateTimeProperty().set(localDateTime);
          } else {
            logger.debug(
                "[CONTROLLER] Failed to update realization date and time - hour and minutes choicbox is empty ");
          }
        });
  }

  private LocalDateTime createDateTime() {
    LocalDate date = realizationDatePicker.getValue();
    String hh = realizationHourChoiceBox.getValue().getName();
    String mm = realizationTimeChoiceBox.getValue().getName();
    LocalDateTime localDateTime =
        LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hh), Integer.parseInt(mm), 0));
    return localDateTime;
  }


    private void configureHourChoiceBox() {
    DictionaryConverter converter = new DictionaryConverter();
    realizationHourChoiceBox.setConverter(converter);
    realizationHourChoiceBox.itemsProperty().bind(visitViewModel.getRealizationHoursValues());
    realizationHourChoiceBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null
                  && realizationHourChoiceBox.getValue() != null
                  && realizationTimeChoiceBox.getValue() != null) {
                LocalDateTime newTime =
                    LocalDateTime.of(
                        realizationDatePicker.getValue().getYear(),
                        realizationDatePicker.getValue().getMonth(),
                        realizationDatePicker.getValue().getDayOfMonth(),
                        Integer.parseInt(realizationHourChoiceBox.getValue().getName()),
                        Integer.parseInt(realizationTimeChoiceBox.getValue().getName()));
                visitViewModel.setViewStartTimeProperty(newTime);
              }
            });
  }

  private void configureMinutesChoiceBox() {
    DictionaryConverter converter = new DictionaryConverter();
    realizationTimeChoiceBox.setConverter(converter);
    realizationTimeChoiceBox.itemsProperty().bind(visitViewModel.getRealizationMinutesValues());
    realizationTimeChoiceBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                LocalDateTime newTime =
                    LocalDateTime.of(
                        realizationDatePicker.getValue().getYear(),
                        realizationDatePicker.getValue().getMonth(),
                        realizationDatePicker.getValue().getDayOfMonth(),
                        Integer.parseInt(realizationHourChoiceBox.getValue().getName()),
                        Integer.parseInt(realizationTimeChoiceBox.getValue().getName()));
                visitViewModel.setViewStartTimeProperty(newTime);
              }
            });
  }

  private void initializePerformer() {
    visitViewModel.initPerformer(performerComboBox);
  }

  private void initializeRegistrant() {
    visitViewModel.initRegistrant(registrantComboBox);
  }

  private void bindPerformer() {
    performerComboBox.setConverter(new DictionaryConverter());
    performerComboBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              visitViewModel.findUserByLogin(newValue.getCode());
            });
    performerComboBox.itemsProperty().bind(visitViewModel.getPerformerValues());
  }

  private void bindRegistrant() {
    registrantComboBox.setConverter(new DictionaryConverter());
    registrantComboBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newPatient) -> {
              visitViewModel.findUserByLogin(newPatient.getCode());
            });
    registrantComboBox.itemsProperty().bind(visitViewModel.getPerformerValues());
  }

  public VisitViewModel getVisitViewModel() {
    return visitViewModel;
  }


}
