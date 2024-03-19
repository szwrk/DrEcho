package net.wilamowski.drecho.client.presentation.examinations.echo;

import javafx.beans.property.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
public class EchoTte {

  private static final Logger logger = LogManager.getLogger(EchoTte.class);

  private final IntegerProperty dm_name = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_ventricle = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_ventricle_systole = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_ventricle_diastole = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_atrium = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_atrium_width = new SimpleIntegerProperty();
  private final IntegerProperty dm_left_atrium_length = new SimpleIntegerProperty();
  private final IntegerProperty dm_aorta = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_ventricle = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_ventricle_systole = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_ventricle_diastole = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_atrium = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_atrium_width = new SimpleIntegerProperty();
  private final IntegerProperty dm_right_atrium_length = new SimpleIntegerProperty();
  private final IntegerProperty dm_pulmonary_trunk = new SimpleIntegerProperty();
  private final IntegerProperty dm_iv_septum_IVS = new SimpleIntegerProperty();
  private final IntegerProperty dm_iv_septum_diastole = new SimpleIntegerProperty();
  private final IntegerProperty dm_iv_septum_systole = new SimpleIntegerProperty();
  private final IntegerProperty dm_posterior_wall = new SimpleIntegerProperty();
  private final IntegerProperty dm_posterior_wall_diastole = new SimpleIntegerProperty();
  private final IntegerProperty dm_posterior_wall_systole = new SimpleIntegerProperty();

  // Doubles
  private final DoubleProperty dm_mass_LVM = new SimpleDoubleProperty();
  private final DoubleProperty dm_relative_wall_thickness = new SimpleDoubleProperty();
  private final DoubleProperty dm_lvmi = new SimpleDoubleProperty();

  // Strings
  private final StringProperty dm_note_txt = new SimpleStringProperty();

  /* Heart valves sections */
  /*  mitral */
  private final IntegerProperty mv_peak_regur_vel = new SimpleIntegerProperty();
  private final IntegerProperty mv_ea_early_to_latediast_trans_mitral_flow_vel =
      new SimpleIntegerProperty();
  private final IntegerProperty mv_eeprim_early_diastolic_mitral_annular_tissue_vel =
      new SimpleIntegerProperty();
  private final IntegerProperty mv_grad_p_p = new SimpleIntegerProperty();
  private final IntegerProperty mv_pressure_half_time = new SimpleIntegerProperty();
  private final IntegerProperty mv_grad_mean = new SimpleIntegerProperty();
  private final IntegerProperty mv_plenimetric_area = new SimpleIntegerProperty();
  private final IntegerProperty mv_propagation_vel = new SimpleIntegerProperty();
  private final IntegerProperty mv_ivrt = new SimpleIntegerProperty();
  private final IntegerProperty mv_deceleration_time = new SimpleIntegerProperty();
  private final IntegerProperty mv_doppler_area = new SimpleIntegerProperty();
  private final IntegerProperty mv_grad_max = new SimpleIntegerProperty();
  private final StringProperty mv_regur_wave_area_short = new SimpleStringProperty();
  private final StringProperty mv_summary = new SimpleStringProperty();

  private final ObjectProperty<PositionFx> mv_regur_wave_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> mv_regur_desc_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> av_regur_wave_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> av_regur_desc_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> tv_regur_wave_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> tv_regur_desc_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> pv_diastolic_function_choosed =
      new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> ot_contratility_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> pv_regur_wave_choosed = new SimpleObjectProperty<>();
  private final ObjectProperty<PositionFx> pv_regur_desc_choosed = new SimpleObjectProperty<>();

  private EchoTte() {}

  public static EchoTte newInstance() {
    logger.debug("Echocardio Tte new instance...");
    return new EchoTte();
  }
}
