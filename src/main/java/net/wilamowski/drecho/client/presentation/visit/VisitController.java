package net.wilamowski.drecho.client.presentation.visit;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TitledPane;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.ViewModels;
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

  @FXML private TitledPane visitVBox;
  private ResourceBundle bundle;
  private VisitDetailsViewModel viewModel;

  public VisitController() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
  }

  @Override
  public void initializeViewModels(ViewModels viewModelsFactory) {
    logger.debug("VisitController init Visit VM...");
    this.viewModel = viewModelsFactory.visitViewModel();
  }

  @Override
  public void postInitialize() {
    realizationDatePicker.valueProperty().bindBidirectional(viewModel.getRealizationDtProperty());
    saveDatePicker.valueProperty().bindBidirectional(viewModel.getViewStartDtProperty());
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
  }

  private void configureHourChoiceBox() {
    DictionaryConverter converter = new DictionaryConverter();
    realizationHourChoiceBox.setConverter(converter);
    realizationHourChoiceBox.itemsProperty().bind(viewModel.getRealizationHoursValues());
    realizationHourChoiceBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                LocalTime newTime =
                    LocalTime.of(
                        Integer.parseInt(realizationHourChoiceBox.getValue().getName()),
                        Integer.parseInt(
                            realizationTimeChoiceBox.getValue() == null
                                ? "0"
                                : realizationTimeChoiceBox.getValue().getName()),
                        0,
                        0);
                viewModel.setViewStartTimeProperty(newTime);
              }
            });
  }

  private void configureMinutesChoiceBox() {
    DictionaryConverter converter = new DictionaryConverter();
    realizationTimeChoiceBox.setConverter(converter);
    realizationTimeChoiceBox.itemsProperty().bind(viewModel.getRealizationMinutesValues());
    realizationTimeChoiceBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                LocalTime newTime =
                    LocalTime.of(
                        Integer.parseInt(realizationHourChoiceBox.getValue().getName()),
                        Integer.parseInt(realizationTimeChoiceBox.getValue().getName()),
                        0,
                        0);
                viewModel.setViewStartTimeProperty(newTime);
              }
            });
  }

  private void initializePerformer() {
    viewModel.initPerformer(performerComboBox);
  }

  private void initializeRegistrant() {
    viewModel.initRegistrant(registrantComboBox);
  }

  private void bindPerformer() {
    performerComboBox.setConverter(new DictionaryConverter());
    performerComboBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              viewModel.selectUserByLogin(newValue.getCode());
            });
    performerComboBox.itemsProperty().bind(viewModel.getPerformerValues());
  }

  private void bindRegistrant() {
    registrantComboBox.setConverter(new DictionaryConverter());
    registrantComboBox
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              viewModel.selectUserByLogin(newValue.getCode());
            });
    registrantComboBox.itemsProperty().bind(viewModel.getPerformerValues());
  }

  public VisitDetailsViewModel getViewModel() {
    return viewModel;
  }
}
