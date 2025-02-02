package net.wilamowski.drecho.standalone.domain.echo;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EchoTteUnmodifable {
  /* Dimensions */
  int dm_name;
  int dm_left_ventricle;
  int dm_left_ventricle_systole;
  int dm_left_ventricle_diastole;
  int dm_left_atrium;
  int dm_left_atrium_width;
  int dm_left_atrium_length;
  int dm_aorta;
  int dm_right_ventricle;
  int dm_right_ventricle_systole;
  int dm_right_ventricle_diastole;
  int dm_right_atrium;
  int dm_right_atrium_width;
  int dm_right_atrium_length;
  int dm_pulmonary_trunk;
  int dm_iv_septum_IVS;
  int dm_iv_septum_diastole;
  int dm_iv_septum_systole;
  int dm_Stringterior_wall;
  int dm_Stringterior_wall_diastole;
  int dm_Stringterior_wall_systole;
  int dm_mass_LVM;
  int dm_lvmi;
  int dm_relative_wall_thickness;
  String dm_note_txt;
  int mv_peak_regur_vel;
  int mv_ea_early_to_latediast_trans_mitral_flow_vel;
  int mv_eeprim_early_diastolic_mitral_annular_tissue_vel;
  int mv_grad_p_p;
  int mv_pressure_half_time;
  int mv_grad_mean;
  int mv_plenimetric_area;
  int mv_propagation_vel;
  int mv_ivrt;
  int mv_deceleration_time;
  int mv_doppler_area;
  int mv_grad_max;
  String mv_regur_wave_area_short;
  String mv_summary;
  String mv_regur_wave_choosed;
  String mv_regur_desc_choosed;
  int av_peak_regur_vel;
  int av_ea_early_to_latediast_trans_aortic_flow_vel;
  int av_eeprim_early_diastolic_aortic_annular_tissue_vel;
  int av_grad_p_p;
  int av_pressure_half_time;
  int av_grad_mean;
  int av_plenimetric_area;
  int av_propagation_vel;
  int av_ivrt;
  int av_deceleration_time;
  int av_doppler_area;
  int av_grad_max;
  String av_regur_wave_area_short;
  String av_summary;
  String av_regur_wave_choosed;
  String av_regur_desc_choosed;
  int tv_peak_regur_vel;
  int tv_ea_early_to_latediast_trans_tricuspid_flow_vel;
  int tv_eeprim_early_diastolic_tricuspid_annular_tissue_vel;
  int tv_grad_p_p;
  int tv_pressure_half_time;
  int tv_grad_mean;
  int tv_plenimetric_area;
  int tv_propagation_vel;
  int tv_ivrt;
  int tv_deceleration_time;
  int tv_doppler_area;
  int tv_grad_max;
  String tv_regur_wave_area_short;
  String tv_summary;
  String tv_regur_wave_choosed;
  String tv_regur_desc_choosed;
  int pv_peak_regur_vel;
  int pv_ea_early_to_latediast_trans_pulmonic_flow_vel;
  int pv_eeprim_early_diastolic_pulmonic_annular_tissue_vel;
  int pv_grad_p_p;
  int pv_pressure_half_time;
  int pv_grad_mean;
  int pv_plenimetric_area;
  int pv_propagation_vel;
  int pv_ivrt;
  int pv_deceleration_time;
  int pv_doppler_area;
  int pv_grad_max;
  String pv_regur_wave_area_short;
  String pv_summary;
  String pv_regur_wave_choosed;
  String pv_regur_desc_choosed;
  String pv_diastolic_function_choosed;
  String ot_contratility_choosed;
  private Long id;

  ///////////////////////////////////////////////////////////////////////

  EchoTteUnmodifable(
      int dm_name,
      int dm_left_ventricle,
      int dm_left_ventricle_systole,
      int dm_left_ventricle_diastole,
      int dm_left_atrium,
      int dm_left_atrium_width,
      int dm_left_atrium_length,
      int dm_aorta,
      int dm_right_ventricle,
      int dm_right_ventricle_systole,
      int dm_right_ventricle_diastole,
      int dm_right_atrium,
      int dm_right_atrium_width,
      int dm_right_atrium_length,
      int dm_pulmonary_trunk,
      int dm_iv_septum_IVS,
      int dm_iv_septum_diastole,
      int dm_iv_septum_systole,
      int dm_Stringterior_wall,
      int dm_Stringterior_wall_diastole,
      int dm_Stringterior_wall_systole,
      int dm_mass_LVM,
      int dm_lvmi,
      int dm_relative_wall_thickness,
      String dm_note_txt,
      int mv_peak_regur_vel,
      int mv_ea_early_to_latediast_trans_mitral_flow_vel,
      int mv_eeprim_early_diastolic_mitral_annular_tissue_vel,
      int mv_grad_p_p,
      int mv_pressure_half_time,
      int mv_grad_mean,
      int mv_plenimetric_area,
      int mv_propagation_vel,
      int mv_ivrt,
      int mv_deceleration_time,
      int mv_doppler_area,
      int mv_grad_max,
      String mv_regur_wave_area_short,
      String mv_summary,
      String mv_regur_wave_choosed,
      String mv_regur_desc_choosed,
      int av_peak_regur_vel,
      int av_ea_early_to_latediast_trans_aortic_flow_vel,
      int av_eeprim_early_diastolic_aortic_annular_tissue_vel,
      int av_grad_p_p,
      int av_pressure_half_time,
      int av_grad_mean,
      int av_plenimetric_area,
      int av_propagation_vel,
      int av_ivrt,
      int av_deceleration_time,
      int av_doppler_area,
      int av_grad_max,
      String av_regur_wave_area_short,
      String av_summary,
      String av_regur_wave_choosed,
      String av_regur_desc_choosed,
      int tv_peak_regur_vel,
      int tv_ea_early_to_latediast_trans_tricuspid_flow_vel,
      int tv_eeprim_early_diastolic_tricuspid_annular_tissue_vel,
      int tv_grad_p_p,
      int tv_pressure_half_time,
      int tv_grad_mean,
      int tv_plenimetric_area,
      int tv_propagation_vel,
      int tv_ivrt,
      int tv_deceleration_time,
      int tv_doppler_area,
      int tv_grad_max,
      String tv_regur_wave_area_short,
      String tv_summary,
      String tv_regur_wave_choosed,
      String tv_regur_desc_choosed,
      int pv_peak_regur_vel,
      int pv_ea_early_to_latediast_trans_pulmonic_flow_vel,
      int pv_eeprim_early_diastolic_pulmonic_annular_tissue_vel,
      int pv_grad_p_p,
      int pv_pressure_half_time,
      int pv_grad_mean,
      int pv_plenimetric_area,
      int pv_propagation_vel,
      int pv_ivrt,
      int pv_deceleration_time,
      int pv_doppler_area,
      int pv_grad_max,
      String pv_regur_wave_area_short,
      String pv_summary,
      String pv_regur_wave_choosed,
      String pv_regur_desc_choosed,
      String pv_diastolic_function_choosed,
      String ot_contratility_choosed) {
    this.dm_name = dm_name;
    this.dm_left_ventricle = dm_left_ventricle;
    this.dm_left_ventricle_systole = dm_left_ventricle_systole;
    this.dm_left_ventricle_diastole = dm_left_ventricle_diastole;
    this.dm_left_atrium = dm_left_atrium;
    this.dm_left_atrium_width = dm_left_atrium_width;
    this.dm_left_atrium_length = dm_left_atrium_length;
    this.dm_aorta = dm_aorta;
    this.dm_right_ventricle = dm_right_ventricle;
    this.dm_right_ventricle_systole = dm_right_ventricle_systole;
    this.dm_right_ventricle_diastole = dm_right_ventricle_diastole;
    this.dm_right_atrium = dm_right_atrium;
    this.dm_right_atrium_width = dm_right_atrium_width;
    this.dm_right_atrium_length = dm_right_atrium_length;
    this.dm_pulmonary_trunk = dm_pulmonary_trunk;
    this.dm_iv_septum_IVS = dm_iv_septum_IVS;
    this.dm_iv_septum_diastole = dm_iv_septum_diastole;
    this.dm_iv_septum_systole = dm_iv_septum_systole;
    this.dm_Stringterior_wall = dm_Stringterior_wall;
    this.dm_Stringterior_wall_diastole = dm_Stringterior_wall_diastole;
    this.dm_Stringterior_wall_systole = dm_Stringterior_wall_systole;
    this.dm_mass_LVM = dm_mass_LVM;
    this.dm_lvmi = dm_lvmi;
    this.dm_relative_wall_thickness = dm_relative_wall_thickness;
    this.dm_note_txt = dm_note_txt;
    this.mv_peak_regur_vel = mv_peak_regur_vel;
    this.mv_ea_early_to_latediast_trans_mitral_flow_vel =
        mv_ea_early_to_latediast_trans_mitral_flow_vel;
    this.mv_eeprim_early_diastolic_mitral_annular_tissue_vel =
        mv_eeprim_early_diastolic_mitral_annular_tissue_vel;
    this.mv_grad_p_p = mv_grad_p_p;
    this.mv_pressure_half_time = mv_pressure_half_time;
    this.mv_grad_mean = mv_grad_mean;
    this.mv_plenimetric_area = mv_plenimetric_area;
    this.mv_propagation_vel = mv_propagation_vel;
    this.mv_ivrt = mv_ivrt;
    this.mv_deceleration_time = mv_deceleration_time;
    this.mv_doppler_area = mv_doppler_area;
    this.mv_grad_max = mv_grad_max;
    this.mv_regur_wave_area_short = mv_regur_wave_area_short;
    this.mv_summary = mv_summary;
    this.mv_regur_wave_choosed = mv_regur_wave_choosed;
    this.mv_regur_desc_choosed = mv_regur_desc_choosed;
    this.av_peak_regur_vel = av_peak_regur_vel;
    this.av_ea_early_to_latediast_trans_aortic_flow_vel =
        av_ea_early_to_latediast_trans_aortic_flow_vel;
    this.av_eeprim_early_diastolic_aortic_annular_tissue_vel =
        av_eeprim_early_diastolic_aortic_annular_tissue_vel;
    this.av_grad_p_p = av_grad_p_p;
    this.av_pressure_half_time = av_pressure_half_time;
    this.av_grad_mean = av_grad_mean;
    this.av_plenimetric_area = av_plenimetric_area;
    this.av_propagation_vel = av_propagation_vel;
    this.av_ivrt = av_ivrt;
    this.av_deceleration_time = av_deceleration_time;
    this.av_doppler_area = av_doppler_area;
    this.av_grad_max = av_grad_max;
    this.av_regur_wave_area_short = av_regur_wave_area_short;
    this.av_summary = av_summary;
    this.av_regur_wave_choosed = av_regur_wave_choosed;
    this.av_regur_desc_choosed = av_regur_desc_choosed;
    this.tv_peak_regur_vel = tv_peak_regur_vel;
    this.tv_ea_early_to_latediast_trans_tricuspid_flow_vel =
        tv_ea_early_to_latediast_trans_tricuspid_flow_vel;
    this.tv_eeprim_early_diastolic_tricuspid_annular_tissue_vel =
        tv_eeprim_early_diastolic_tricuspid_annular_tissue_vel;
    this.tv_grad_p_p = tv_grad_p_p;
    this.tv_pressure_half_time = tv_pressure_half_time;
    this.tv_grad_mean = tv_grad_mean;
    this.tv_plenimetric_area = tv_plenimetric_area;
    this.tv_propagation_vel = tv_propagation_vel;
    this.tv_ivrt = tv_ivrt;
    this.tv_deceleration_time = tv_deceleration_time;
    this.tv_doppler_area = tv_doppler_area;
    this.tv_grad_max = tv_grad_max;
    this.tv_regur_wave_area_short = tv_regur_wave_area_short;
    this.tv_summary = tv_summary;
    this.tv_regur_wave_choosed = tv_regur_wave_choosed;
    this.tv_regur_desc_choosed = tv_regur_desc_choosed;
    this.pv_peak_regur_vel = pv_peak_regur_vel;
    this.pv_ea_early_to_latediast_trans_pulmonic_flow_vel =
        pv_ea_early_to_latediast_trans_pulmonic_flow_vel;
    this.pv_eeprim_early_diastolic_pulmonic_annular_tissue_vel =
        pv_eeprim_early_diastolic_pulmonic_annular_tissue_vel;
    this.pv_grad_p_p = pv_grad_p_p;
    this.pv_pressure_half_time = pv_pressure_half_time;
    this.pv_grad_mean = pv_grad_mean;
    this.pv_plenimetric_area = pv_plenimetric_area;
    this.pv_propagation_vel = pv_propagation_vel;
    this.pv_ivrt = pv_ivrt;
    this.pv_deceleration_time = pv_deceleration_time;
    this.pv_doppler_area = pv_doppler_area;
    this.pv_grad_max = pv_grad_max;
    this.pv_regur_wave_area_short = pv_regur_wave_area_short;
    this.pv_summary = pv_summary;
    this.pv_regur_wave_choosed = pv_regur_wave_choosed;
    this.pv_regur_desc_choosed = pv_regur_desc_choosed;
    this.pv_diastolic_function_choosed = pv_diastolic_function_choosed;
    this.ot_contratility_choosed = ot_contratility_choosed;
  }
}
