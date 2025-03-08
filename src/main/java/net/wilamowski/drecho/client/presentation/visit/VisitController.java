package net.wilamowski.drecho.client.presentation.visit;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.ToString;
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
public class VisitController implements Initializable {
  private static final Logger logger = LogManager.getLogger(VisitController.class);

  @FXML private ComboBox<PositionFx> performerComboBox;

  @FXML private ComboBox<PositionFx> registrantComboBox;

  @FXML private DatePicker realizationDatePicker;

  @FXML private ChoiceBox<PositionFx> realizationHourChoiceBox;
  @FXML private ChoiceBox<PositionFx> realizationMinutesChoiceBox;

  @FXML private DatePicker saveDatePicker;
  @FXML private Label idLabel;
  @FXML private Label statusLabel;

  @FXML private TitledPane visitVBox;
  private ResourceBundle bundle;
  private VisitViewModel visitViewModel;
  private DictionaryConverter realizationHoursConverter;
  private DictionaryConverter realizationMinutesConverter;
  private DictionaryConverter registrantConverter;
  private DictionaryConverter performerConverter;

    public VisitController(VisitViewModel visitViewModel) {
        this.visitViewModel = visitViewModel;
    }

    @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
      logger.debug("VisitController init Visit VM...");
      this.realizationHoursConverter = new DictionaryConverter(visitViewModel.getDictService(), "VST_REALIZ_HOURS");
      this.realizationMinutesConverter = new DictionaryConverter(visitViewModel.getDictService(), "VST_REALIZ_MIN");
      this.registrantConverter = new DictionaryConverter( visitViewModel.getDictService() , "PRSREGI");
      this.performerConverter = new DictionaryConverter( visitViewModel.getDictService() ,"PRSPERF" );


      updateRealizationDateTimeListener(realizationDatePicker.valueProperty());
      updateRealizationDateTimeListener(realizationHourChoiceBox.valueProperty());
      updateRealizationDateTimeListener( realizationMinutesChoiceBox.valueProperty());
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
      selectRealizationDateTime(LocalDateTime.now( ));
  }





    private void selectRealizationDateTime(LocalDateTime localDateTime) {
        logger.traceEntry();
        selectDateOnDatePicker( localDateTime );
        int currentHour = localDateTime.getHour();
        int actualMinute = localDateTime.getMinute();
        selectHourOnComboBox( currentHour );
        if  ( isHourPositionNoExistsOnList( ) ){
            return;
        }
        //check is office hours
        selectMinuteOnComboBox( actualMinute );
        logger.traceExit();
    }

    private boolean isHourPositionNoExistsOnList() {
        return realizationHourChoiceBox.getSelectionModel( ).isEmpty( );
    }

    private void selectHourOnComboBox(int currentHour) {
        Optional<PositionFx> hourPositionOptional = realizationHourChoiceBox.getItems( ).stream( )
                .filter( position -> Integer.parseInt( position.getName( )) == currentHour )
                .findFirst( );

        if (hourPositionOptional.isPresent()){
            realizationHourChoiceBox.getSelectionModel().select(hourPositionOptional.get());
        } else {
            //consume
        }
    }

    private void selectDateOnDatePicker(LocalDateTime localDateTime) {
        realizationDatePicker.setValue( localDateTime.toLocalDate());
    }

    private void selectMinuteOnComboBox(int actualMinute) {
        List<PositionFx> matchingPosition = realizationMinutesChoiceBox.getItems().stream()
                .filter(position -> {
                    try {
                        return Integer.parseInt(position.getName()) < actualMinute;
                    } catch (NumberFormatException e) {
                        logger.error( e.getMessage() );
                        return false;
                    }
                })
                .toList( );

        Optional<PositionFx> maxPosition = matchingPosition.stream()
                .max( Comparator.comparingInt( p -> Integer.parseInt(p.getName())));

        maxPosition.ifPresent(position -> {
            realizationMinutesChoiceBox.getSelectionModel().select(position);
        });
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
              && realizationMinutesChoiceBox.getValue() != null) {
            LocalDateTime localDateTime = createDateTime();
            logger.debug("[CONTROLLER] Updated realization date and time: {}", localDateTime);
            visitViewModel.getRealizationDateTimeProperty().set(localDateTime);
          } else {
            logger.warn(
                "[CONTROLLER] Failed to update realization date and time - hour and minutes choicbox is empty ");
          }
        });
  }

  private LocalDateTime createDateTime() {
    LocalDate date = realizationDatePicker.getValue();
    String hh = realizationHourChoiceBox.getValue().getName();
    String mm = realizationMinutesChoiceBox.getValue().getName();
    LocalDateTime localDateTime =
        LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hh), Integer.parseInt(mm), 0));
    return localDateTime;
  }


    private void configureHourChoiceBox() {
    realizationHourChoiceBox.setConverter( realizationHoursConverter );
    realizationHourChoiceBox.itemsProperty().bind(visitViewModel.getRealizationHoursValues());
    realizationHourChoiceBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null
                  && realizationHourChoiceBox.getValue() != null
                  && realizationMinutesChoiceBox.getValue() != null) {
                LocalDateTime newTime =
                    LocalDateTime.of(
                        realizationDatePicker.getValue().getYear(),
                        realizationDatePicker.getValue().getMonth(),
                        realizationDatePicker.getValue().getDayOfMonth(),
                        Integer.parseInt(realizationHourChoiceBox.getValue().getName()),
                        Integer.parseInt( realizationMinutesChoiceBox.getValue().getName()));
                visitViewModel.setViewStartTimeProperty(newTime);
              }
            });
  }

  private void configureMinutesChoiceBox() {

    realizationMinutesChoiceBox.setConverter( realizationMinutesConverter );
    realizationMinutesChoiceBox.itemsProperty().bind(visitViewModel.getRealizationMinutesValues());
    realizationMinutesChoiceBox
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
                        Integer.parseInt( realizationMinutesChoiceBox.getValue().getName()));
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
    performerComboBox.setConverter( performerConverter );
    performerComboBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              visitViewModel.findUserByLogin(newValue.getCode());
            });
    performerComboBox.itemsProperty().bind(visitViewModel.getPerformerValues());
  }

  private void bindRegistrant() {
    registrantComboBox.setConverter( registrantConverter );
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
