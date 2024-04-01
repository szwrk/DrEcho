package net.wilamowski.drecho.client.presentation.examinations.echo;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.wilamowski.drecho.client.presentation.calculators.CalculatorViewModel;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.dictionaries.general.ListLoader;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.client.presentation.examinations.general.GeneralExaminationController;
import net.wilamowski.drecho.connectors.model.ConnectorEchoTte;
import net.wilamowski.drecho.connectors.model.ConnectorSimpleDictionaries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
@Getter(AccessLevel.PACKAGE)
@Setter
public class EchoTteViewModel {
  private static final Logger logger = LogManager.getLogger(EchoTteViewModel.class);
  private static Map<String, String> dictionaries;
  public GeneralExaminationController generalExaminationController;
  private ResourceBundle bundle;
  private CalculatorViewModel calculator;
  private ConnectorEchoTte dataModel;
  private ConnectorSimpleDictionaries dictService;

  private ObjectProperty<EchoTte> echoTteProperty;
  /* Comboboxes, list of values and current choose item*/
  // mitral
  private ListProperty<PositionFx> mv_regur_wave_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private ListProperty<PositionFx> mv_regur_desc_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  // aortal
  private ListProperty<PositionFx> av_regur_wave_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private ListProperty<PositionFx> av_regur_desc_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  // tri
  private ListProperty<PositionFx> tv_regur_wave_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  /* Heart dimensions section */
  // ints
  private ListProperty<PositionFx> tv_regur_desc_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  // pulmo
  private ListProperty<PositionFx> pv_regur_wave_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private ListProperty<PositionFx> pv_regur_desc_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private ListProperty<PositionFx> pv_diastolic_function_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  // others
  private ListProperty<PositionFx> ot_contratility_values =
      new SimpleListProperty<>(FXCollections.observableArrayList());

  private EchoTteViewModel() {}

  public EchoTteViewModel(ConnectorEchoTte dataModel, ConnectorSimpleDictionaries service) {
    this.dataModel = dataModel;
    this.calculator = new CalculatorViewModel();
    this.dictService = service;
    initDictionaries();
  }

  private void initDictionaries() {
    ListLoader loader = ListLoader.source(dictService);
    loader.loadValuesToFxList("MVREGWAV", mv_regur_wave_values);
    loader.loadValuesToFxList("AVREGWAV", av_regur_wave_values);
    loader.loadValuesToFxList("TVREGWAV", tv_regur_wave_values);
    loader.loadValuesToFxList("PVREGWAV", pv_regur_wave_values);
    loader.loadValuesToFxList("PVDSTFN", pv_diastolic_function_values);
    loader.loadValuesToFxList("MVDESCTXT", mv_regur_desc_values);
    loader.loadValuesToFxList("AVDESCTXT", av_regur_desc_values);
    loader.loadValuesToFxList("TVDESCTXT", tv_regur_desc_values);
    loader.loadValuesToFxList("PVDESCTXT", pv_regur_desc_values);
    loader.loadValuesToFxList("OTCNTTXT", ot_contratility_values);
  }

  public void initEchoTteFormData(EchoTte echo) {
    this.echoTteProperty = new SimpleObjectProperty<>(echo);
  }

  public EchoTte getEchoTteProperty() {
    return echoTteProperty.get();
  }

  public void saveChanges() {}

  private void clearForm() {}

  //  public ModelEchoTte getDataModel() {
  //    return dataModel;
  //  }

  public ResourceBundle getBundle() {
    return bundle;
  }

  void autoCalculateLVMIndex() {
    registerCalculateListener(
        echoTteProperty.get().dm_left_ventricle_diastole(),
        "dm_left_ventricle_diastole",
        calculateLvmIndex());
    registerCalculateListener(
        echoTteProperty.get().dm_iv_septum_diastole(),
        "dm_iv_septum_diastole",
        calculateLvmIndex());
    registerCalculateListener(
        echoTteProperty.get().dm_posterior_wall_diastole(),
        "dm_posterior_wall_diastole",
        calculateLvmIndex());
  }

  private void registerCalculateListener( // todo debug property
      Property control, String propertyName, Runnable calculation) {
    try {
      control.addListener(
          (observable, oldValue, newValue) -> {
            logger.debug("{}: {}", propertyName, newValue);
            calculation.run();
          });
    } catch (Exception e) {
      handleError(e, "e.999.header", "e.999.msg");
    }
  }

  private void handleError(Exception e, String headerKey, String contentKey) {
    logger.error("An error occurred:", e.getMessage(), e);
    String header = bundle.getString(headerKey);
    String msg = bundle.getString(contentKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  public Runnable calculateLvmIndex() {
    return () -> {
      logger.warn("Add patient height and weight to context");
      logger.traceEntry();
      EchoTte echo = this.echoTteProperty.get();
      Double calcResult =
          calculator.calcLvmIndex(
              echo.dm_left_ventricle_diastole(),
              echo.dm_iv_septum_diastole(),
              echo.dm_left_ventricle_diastole());
      Platform.runLater(() -> echo.dm_lvmi().set(calcResult));
    };
  }

  void autoCalculateRTW() {
    registerCalculateListener(
        echoTteProperty.get().dm_left_ventricle_diastole(),
        "dm_left_ventricle_diastole",
        calculateRwt());
    registerCalculateListener(
        echoTteProperty.get().dm_posterior_wall_diastole(),
        "dm_posterior_wall_diastole",
        calculateRwt());
  }

  public Runnable calculateRwt() {
    return () -> {
      logger.debug("Start calculating RWT...");
      EchoTte echo = this.echoTteProperty.get();
      Double calcResult =
          calculator.calcRwt(echo.dm_posterior_wall_diastole(), echo.dm_left_ventricle_diastole());
      Platform.runLater(() -> echo.dm_relative_wall_thickness().set(calcResult));
    };
  }

  void autoCalculateLVM() {
    registerCalculateListener(
        echoTteProperty.get().dm_left_ventricle_diastole(),
        "dm_left_ventricle_diastole",
        calculateLvm());
    registerCalculateListener(
        echoTteProperty.get().dm_iv_septum_diastole(), "dm_iv_septum_diastole", calculateLvm());
    registerCalculateListener(
        echoTteProperty.get().dm_posterior_wall_diastole(),
        "dm_posterior_wall_diastole",
        calculateLvm());
  }

  public Runnable calculateLvm() {
    return () -> {
      logger.warn("Add patient height and weight to context");
      logger.traceEntry();
      EchoTte echo = this.echoTteProperty.get();
      Double calcResult =
          calculator.calcLvm(
              echo.dm_left_ventricle_diastole(),
              echo.dm_iv_septum_diastole(),
              echo.dm_posterior_wall_diastole());
      Platform.runLater(() -> echo.dm_mass_LVM().set(calcResult));
    };
  }
}
