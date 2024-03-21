package net.wilamowski.drecho.client.presentation.examinations.echo;

import atlantafx.base.controls.Popover;
import atlantafx.base.util.Animations;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModels;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.customs.PopoverFactory;
import net.wilamowski.drecho.client.presentation.customs.controls.FormContextMenu;
import net.wilamowski.drecho.client.presentation.customs.inputfilter.Converters;
import net.wilamowski.drecho.client.presentation.customs.inputfilter.FormatterApllier;
import net.wilamowski.drecho.client.presentation.customs.inputfilter.Formatters;
import net.wilamowski.drecho.client.presentation.debugger.DebugHandler;
import net.wilamowski.drecho.client.presentation.debugger.KeyDebugHandlerGui;
import net.wilamowski.drecho.client.presentation.dictionaries.general.DictionaryConverter;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class EchoController
    implements Initializable,
        KeyEventDebugInitializer,
        Tooltipable,
        ViewModelsInitializer,
        ViewHandlerInitializer,
        PostInitializable {
  private static final Logger logger = LogManager.getLogger(EchoController.class);
  private final Converters converterFactory;
  public String finalKeyCombinationDebug;
  private EchoTteViewModel viewModel;
  private ViewModels factory;
  private URL location;
  @FXML private VBox aortal;
  @FXML private TextField av_acc_rate_pressure_half_time;
  @FXML private TextField av_max_grad;
  @FXML private TextField av_mean_grad;
  @FXML private TextField av_plenimetric_area;
  @FXML private TextField av_regur_vmax;
  @FXML private ComboBox<PositionFx> av_regur_waveComboBox;
  @FXML private TextArea av_summary_textArea;
  @FXML private ComboBox<PositionFx> av_valve_descComboBox;
  @FXML private VBox dimensions;
  @FXML private TextField dm_aorta;
  @FXML private TextField dm_iv_septum_diastole;
  @FXML private TextField dm_iv_septum_systole;
  @FXML private TextField dm_left_atrium_length;
  @FXML private TextField dm_left_atrium_width;
  @FXML private TextField dm_left_ventricle_diastole;
  @FXML private TextField dm_left_ventricle_systole;
  @FXML private TextField dm_lvmi;
  @FXML private TextField dm_mass_LVM;
  @FXML private Label dm_name_lbl;
  @FXML private TextArea dm_note_txt;
  @FXML private TextField dm_posterior_wall_systole;
  @FXML private TextField dm_posterior_wall_diastole;
  @FXML private TextField dm_pulmonary_trunk;
  @FXML private TextField dm_relative_wall_thickness;
  @FXML private TextField dm_right_atrium_length;
  @FXML private TextField dm_right_atrium_width;

  //  @FXML private TextField dm_right_ventricle_diastole;
  @FXML private TextField dm_right_ventricle;
  @FXML private HBox dm_section;
  @FXML private VBox lung;
  @FXML private VBox mitral;
  @FXML private TextField mv_deceleration_time;
  @FXML private TextField mv_doppler_area;
  @FXML private TextField mv_ea_early_to_latediast_trans_mitral_flow_vel;
  @FXML private TextField mv_eeprim_early_diastolic_mitral_annular_tissue_vel;
  @FXML private TextField mv_grad_max;
  @FXML private TextField mv_grad_mean;
  @FXML private Label mv_grad_p_p;
  @FXML private TextField mv_ivrt;
  @FXML private TextField mv_peak_regur_vel;
  @FXML private TextField mv_plenimetric_area;
  @FXML private TextField mv_pressure_half_time;
  @FXML private TextField mv_propagation_vel;
  @FXML private ComboBox<PositionFx> mv_regur_waveComboBox;
  @FXML private TextField mv_regur_wave_area_short;
  @FXML private TextArea mv_summary;
  @FXML private ComboBox<PositionFx> mv_valve_descComboBox;
  @FXML private TextArea ot_conclusion;
  @FXML private TextArea ot_contractility_desc;
  @FXML private ComboBox<PositionFx> ot_contractility_dictComboBox;
  @FXML private TextField ot_ejection_fraction;
  @FXML private TextArea ot_others_txt;
  @FXML private TextArea ot_pericardium_txt;
  @FXML private TextField ot_tapse;
  @FXML private VBox others;
  @FXML private TextField pv_acct;
  @FXML private ComboBox<PositionFx> pv_diastolic_function;
  @FXML private ComboBox<PositionFx> pv_valve_descCombBox;
  @FXML private TextField pv_dpap;
  @FXML private TextField pv_mpap;
  @FXML private TextField pv_qpqs;
  @FXML private TextField pv_regur_grad_max;
  @FXML private TextField pv_regur_vmax;
  @FXML private ComboBox<PositionFx> pv_regur_waveComboBox;
  @FXML private TextArea pv_summary_txt;
  @FXML private VBox root;
  @FXML private VBox tri;
  @FXML private ComboBox<PositionFx> tv_valve_descComboBox;
  @FXML private TextField tv_jet_area;
  @FXML private TextField tv_max_grad;
  @FXML private TextField tv_p12t;
  @FXML private TextField tv_planimetric_area;
  @FXML private TextField tv_regur_vmax;
  @FXML private ComboBox<PositionFx> tv_regur_waveComboBox;
  @FXML private TextField tv_spap;
  @FXML private TextArea tv_summary_txt;
  @FXML private TextField tv_trv_max;
  @FXML private VBox valves;
  private GeneralViewHandler viewHandler;
  private FormContextMenu contextMenuCreator;
  private Formatters formatters;
  private FormatterApllier apply;
  private String requiredFieldAnimationType;
  private Popover lvmPopover;
  private Popover rwtPopover;
  private Popover lvmiPopover;

  // fxml fields end

  public EchoController() {
    this.converterFactory = new Converters();
  }

  @FXML
  void initialize() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    apply = new FormatterApllier();
    formatters = Formatters.newFactory();
  }

  @FXML
  public void onActionPrint(javafx.event.ActionEvent actionEvent) {
    logger.debug("Clicked on print...");
  }

  @FXML
  public void onActionOnLvmHelp(ActionEvent event) {
    logger.debug("Clicked on lvm mass help");
    lvmPopover.show(dm_mass_LVM);
  }

  @FXML
  public void onActionRwtHelp(ActionEvent event) {
    logger.debug("Clicked on rwt help");
    rwtPopover.show(dm_relative_wall_thickness);
  }

  @FXML
  public void onActionOnLvmiHelp(ActionEvent event) {
    logger.debug("Clicked on lvmi help");
    lvmiPopover.show(dm_lvmi);
  }

  @Override
  public void initializeKeyEventDebugging() {
    logger.traceEntry();
    DebugHandler debugHandler = new KeyDebugHandlerGui();
    debugHandler.initNode(root);
    debugHandler.watch(this);
    logger.traceExit();
  }

  @Override
  public Node getRootUiNode() {
    return root;
  }

  @Override
  public void initializeViewModels(ViewModels factory) {
    logger.traceEntry();
    this.factory = factory;
    this.viewModel = factory.exhaminationTteViewModel();
    Objects.requireNonNull(viewModel);
    logger.traceExit();
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void postInitialize() {}

  void reinitializeEchoFormProperties(EchoTte echoTte) {
    viewModel.initEchoTteFormData(echoTte);
    // dimension
    heartDimensionSectionBindings();
    configureDimensionsInputsFormatters();
    heartDimensionSectionInitDebugListeners();
    // mitral
    configureMitralValveTextFields();
    // aortal
    configureAortalValveInputs();
    // tri
    configureTriValveInputs();
    // pulmo
    configurePulmoInputs();
    // others
    configureOthersSectionInputs();
    // apply to all
    connectComboBoxToViewModel();
    //
    contextMenuCreator = FormContextMenu.setup(viewHandler);
    contextMenuCreator.applyForRootNode(root);
    // calcs
    initAnimationTypeForCalculation();
    viewModel.autoCalculateLVM();
    configLvmInfoBox();

    viewModel.autoCalculateRTW();
    configRwtInfoBox();

    viewModel.autoCalculateLVMIndex();
    configLvmIndexInfoBox();
  }

  private void configureDimensionsInputsFormatters() {
    apply.setFormatter(dm_left_ventricle_systole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_left_ventricle_diastole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_left_atrium_width, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_left_atrium_length, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_right_ventricle, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_right_atrium_width, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_right_atrium_length, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_pulmonary_trunk, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_iv_septum_diastole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_iv_septum_systole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_posterior_wall_diastole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_posterior_wall_systole, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_mass_LVM, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(dm_lvmi, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(dm_aorta, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(dm_relative_wall_thickness, formatters.createFormatterBy("DOUBLE"));
  }

  void heartDimensionSectionBindings() {
    logger.debug("Heart dimensions bindings starting...");
    if (getViewModel().getEchoTteProperty() == null) {
      logger.warn("Echo is null. ");
      throw new IllegalStateException("Echo is null");
    }

    EchoTte echo = viewModel.getEchoTteProperty();

    Bindings.bindBidirectional(
        dm_aorta.textProperty(), echo.dm_aorta(), converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_iv_septum_diastole.textProperty(),
        echo.dm_iv_septum_diastole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_iv_septum_systole.textProperty(),
        echo.dm_iv_septum_systole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_left_atrium_length.textProperty(),
        echo.dm_left_atrium_length(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_left_atrium_width.textProperty(),
        echo.dm_left_atrium_width(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_left_ventricle_systole.textProperty(),
        echo.dm_left_ventricle_systole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_lvmi.textProperty(), echo.dm_lvmi(), converterFactory.createConverterBy("DOUBLE"));
    Bindings.bindBidirectional(
        dm_mass_LVM.textProperty(),
        echo.dm_mass_LVM(),
        converterFactory.createConverterBy("DOUBLE"));
    Bindings.bindBidirectional(dm_note_txt.textProperty(), echo.dm_note_txt());
    Bindings.bindBidirectional(
        dm_left_ventricle_diastole.textProperty(),
        echo.dm_left_ventricle_diastole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_posterior_wall_systole.textProperty(),
        echo.dm_posterior_wall_systole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_posterior_wall_diastole.textProperty(),
        echo.dm_posterior_wall_diastole(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_pulmonary_trunk.textProperty(),
        echo.dm_pulmonary_trunk(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_relative_wall_thickness.textProperty(),
        echo.dm_relative_wall_thickness(),
        converterFactory.createConverterBy("DOUBLE"));
    Bindings.bindBidirectional(
        dm_right_atrium_length.textProperty(),
        echo.dm_right_atrium_length(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_right_atrium_width.textProperty(),
        echo.dm_right_atrium_width(),
        converterFactory.createConverterBy("INTEGER_3"));
    Bindings.bindBidirectional(
        dm_right_ventricle.textProperty(),
        echo.dm_right_ventricle(),
        converterFactory.createConverterBy("INTEGER_3"));
  }

  public EchoTteViewModel getViewModel() {
    return viewModel;
  }

  void heartDimensionSectionInitDebugListeners() {
    if (getViewModel().getEchoTteProperty() == null) {
      logger.warn("Echo is null. ");
      throw new IllegalStateException("Echo is null");
    }

    EchoTte echo = viewModel.getEchoTteProperty();

    dm_note_txt
        .textProperty()
        .addListener(
            (obs, val1, val2) -> {
              logger.debug(
                  "Clic on dm_left_ventricle_diastole val1 {} val2 {} vm {}",
                  val1,
                  val2,
                  echo.dm_note_txt().get());

              logger.debug(echo.hashCode());
            });
    dm_left_ventricle_diastole
        .textProperty()
        .addListener(
            (obs, val1, val2) ->
                logger.debug(
                    "clic on dm_left_ventricle_diastole val1 {} val2 {} vm {}",
                    val1,
                    val2,
                    echo.dm_left_ventricle_diastole()));
  }

  private void configureMitralValveTextFields() {
    apply.setFormatter(mv_peak_regur_vel, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(
        mv_ea_early_to_latediast_trans_mitral_flow_vel, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(
        mv_eeprim_early_diastolic_mitral_annular_tissue_vel,
        formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_grad_mean, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_plenimetric_area, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(mv_propagation_vel, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_ivrt, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_deceleration_time, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_doppler_area, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(mv_grad_max, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_pressure_half_time, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_regur_wave_area_short, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(mv_summary, formatters.createFormatterBy("TEXT"));
  }

  private void configureAortalValveInputs() {
    apply.setFormatter(av_regur_vmax, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(av_mean_grad, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(av_max_grad, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(av_acc_rate_pressure_half_time, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(av_plenimetric_area, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(av_summary_textArea, formatters.createFormatterBy("INTEGER_3"));
  }

  private void configureTriValveInputs() {
    apply.setFormatter(tv_regur_vmax, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(tv_p12t, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(tv_max_grad, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(tv_jet_area, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(tv_planimetric_area, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(tv_spap, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(tv_trv_max, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(tv_summary_txt, formatters.createFormatterBy("INTEGER_3"));
  }

  private void configurePulmoInputs() {
    apply.setFormatter(pv_regur_vmax, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(pv_mpap, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(pv_regur_grad_max, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(pv_acct, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(pv_dpap, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(pv_qpqs, formatters.createFormatterBy("DOUBLE"));
    apply.setFormatter(pv_summary_txt, formatters.createFormatterBy("INTEGER_3"));
  }

  private void configureOthersSectionInputs() {
    apply.setFormatter(ot_pericardium_txt, formatters.createFormatterBy("TEXT"));
    apply.setFormatter(ot_others_txt, formatters.createFormatterBy("TEXT"));
    apply.setFormatter(ot_contractility_desc, formatters.createFormatterBy("TEXT"));
    apply.setFormatter(ot_ejection_fraction, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(ot_tapse, formatters.createFormatterBy("INTEGER_3"));
    apply.setFormatter(ot_conclusion, formatters.createFormatterBy("TEXT"));
  }

  private void initAnimationTypeForCalculation() {
    requiredFieldAnimationType =
        ClientPropertyReader.getString("examination.echotte.lvm.animation");
  }

  private void configLvmInfoBox() {
    final String expression = ClientPropertyReader.getString("examination.echotte.lvm.expression");
    final String referenceValues =
        ClientPropertyReader.getString("examination.echotte.lvm.reference");
    lvmPopover =
        PopoverFactory.createActionPopover(
            "LVM - Left ventricular mass",
            String.format("Wyrażenie: %s\n%s", expression, referenceValues),
            "Highlight calculated fields",
            "Tip: After clicking, the interface will perform an animation indicating the fields required for this calculation",
            event ->
                showRequiredFieldAnimation(
                    requiredFieldAnimationType,
                    dm_left_ventricle_diastole,
                    dm_iv_septum_diastole,
                    dm_posterior_wall_diastole),
            Popover.ArrowLocation.LEFT_TOP);
  }

  private void showRequiredFieldAnimation(String animationType, Node... node) {
    if (animationType.equals("1")) {
      List<Node> nodes = Arrays.asList(node);
      nodes.stream()
          .map(Animations::flash)
          .collect(Collectors.toSet())
          .forEach(Animation::playFromStart);
    } else if (animationType.equals("2")) {
      List<Node> nodes = Arrays.asList(node);
      nodes.stream()
          .map(node1 -> Animations.zoomIn(node1, Duration.millis(1000)))
          .collect(Collectors.toSet())
          .forEach(Animation::playFromStart);
    } else {
      logger.warn(String.format("Settings %s is empty: ", animationType));
    }
  }

  private void configRwtInfoBox() {
    final String expression = ClientPropertyReader.getString("examination.echotte.rwt.expression");
    rwtPopover =
        PopoverFactory.createActionPopover(
            "RWT - Relative Wall Thickness",
            "Wyrażenie: " + expression,
            "Highlight calculated fields",
            "Tip: After clicking, the interface will perform an animation indicating the fields required for this calculation",
            event ->
                showRequiredFieldAnimation(
                    requiredFieldAnimationType,
                    dm_left_ventricle_diastole,
                    dm_posterior_wall_diastole),
            Popover.ArrowLocation.LEFT_TOP);
  }

  private void configLvmIndexInfoBox() {
    final String expression = ClientPropertyReader.getString("examination.echotte.lvmi.expression");
    final String referenceValues =
        ClientPropertyReader.getString("examination.echotte.lvm.reference");
    lvmiPopover =
        PopoverFactory.createActionPopover(
            "LVMI - LV Mass Indexed to Body Surface Area",
            String.format("Expression: %s\n%s", expression, referenceValues),
            "Highlight calculated fields",
            "Tip: After clicking, the interface will perform an animation indicating the fields required for this calculation",
            event ->
                showRequiredFieldAnimation(
                    requiredFieldAnimationType,
                    dm_left_ventricle_diastole,
                    dm_iv_septum_diastole,
                    dm_posterior_wall_diastole),
            // for preview
            Popover.ArrowLocation.LEFT_TOP);
  }

  private void connectComboBoxToViewModel() {
    DictionaryConverter converter = new DictionaryConverter();
    // mv
    mv_regur_waveComboBox.itemsProperty().bind(viewModel.getMv_regur_wave_values());
    mv_regur_waveComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().mv_regur_wave_choosed());
    mv_regur_waveComboBox.setConverter(converter);

    mv_valve_descComboBox.itemsProperty().bind(viewModel.getMv_regur_desc_values());
    mv_valve_descComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().mv_regur_desc_choosed());
    mv_valve_descComboBox.setConverter(converter);
    // av
    av_regur_waveComboBox.itemsProperty().bind(viewModel.getAv_regur_wave_values());
    av_regur_waveComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().av_regur_wave_choosed());
    av_regur_waveComboBox.setConverter(converter);

    av_valve_descComboBox.itemsProperty().bind(viewModel.getAv_regur_desc_values());
    av_valve_descComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().av_regur_desc_choosed());
    av_valve_descComboBox.setConverter(converter);
    // tv
    tv_regur_waveComboBox.itemsProperty().bind(viewModel.getTv_regur_wave_values());
    tv_regur_waveComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().tv_regur_wave_choosed());
    tv_regur_waveComboBox.setConverter(converter);

    tv_valve_descComboBox.itemsProperty().bind(viewModel.getTv_regur_desc_values());
    tv_valve_descComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().tv_regur_desc_choosed());
    tv_valve_descComboBox.setConverter(converter);
    // pv
    pv_regur_waveComboBox.itemsProperty().bind(viewModel.getPv_regur_wave_values());
    pv_regur_waveComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().pv_regur_wave_choosed());
    pv_regur_waveComboBox.setConverter(converter);

    pv_valve_descCombBox.itemsProperty().bind(viewModel.getPv_regur_desc_values());
    pv_valve_descCombBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().pv_regur_desc_choosed());
    pv_valve_descCombBox.setConverter(converter);

    pv_diastolic_function.itemsProperty().bind(viewModel.getPv_diastolic_function_values());
    pv_diastolic_function
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().pv_diastolic_function_choosed());
    pv_diastolic_function.setConverter(converter);
    // ot
    ot_contractility_dictComboBox.itemsProperty().bind(viewModel.getOt_contratility_values());
    ot_contractility_dictComboBox
        .valueProperty()
        .bindBidirectional(viewModel.getEchoTteProperty().ot_contratility_choosed());
    ot_contractility_dictComboBox.setConverter(converter);
  }

  public void clearForm() {
    logger.debug("Clear form...");
    dm_aorta.setText("");
    dm_iv_septum_diastole.setText("");
    dm_iv_septum_systole.setText("");
    dm_left_atrium_length.setText("");
    dm_left_atrium_width.setText("");
    dm_left_ventricle_diastole.setText("");
    dm_left_ventricle_systole.setText("");
    dm_lvmi.setText("");
    dm_mass_LVM.setText("");
    dm_name_lbl.setText("");
    dm_note_txt.setText("");
    dm_posterior_wall_systole.setText("");
    dm_posterior_wall_diastole.setText("");
    dm_pulmonary_trunk.setText("");
    dm_relative_wall_thickness.setText("");
    dm_right_atrium_length.setText("");
    dm_right_atrium_width.setText("");
  }
}
