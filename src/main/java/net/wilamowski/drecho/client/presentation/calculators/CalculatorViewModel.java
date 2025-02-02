package net.wilamowski.drecho.client.presentation.calculators;

import javafx.beans.property.IntegerProperty;
import net.wilamowski.drecho.standalone.domain.echo.EchoCardioCalculator;
import net.wilamowski.drecho.standalone.domain.echo.EchoCardioCalculatorExpressionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class CalculatorViewModel {
  public static final int HEIGHT_IN_CM = 190; // todo hardcoded value!
  public static final int WEIGHT_IN_KG = 83; // todo hardcoded value!
  private static final Logger logger = LogManager.getLogger(CalculatorViewModel.class);
  private final EchoCardioCalculator calculator;

  public CalculatorViewModel() {
    this.calculator = new EchoCardioCalculatorExpressionImpl();
  }

  public Double calcLvm(
      IntegerProperty dm_left_ventricle_diastole,
      IntegerProperty dm_iv_septum_diastole,
      IntegerProperty dm_posterior_wall_diastole) {
    if (validateLvmInputs(
        dm_left_ventricle_diastole, dm_iv_septum_diastole, dm_posterior_wall_diastole)) {
      logger.debug("Start calculating LVMI...");
      Double v =
          calculator.calcLeftVentricularMass(
              dm_left_ventricle_diastole.get(),
              dm_iv_septum_diastole.get(),
              dm_posterior_wall_diastole.get());
      return v;
    } else {
      logger.debug("one or more required field is null");
      return (double) 0;
    }
  }

  private boolean validateLvmInputs(
      IntegerProperty dm_left_ventricle_diastole,
      IntegerProperty dm_iv_septum_diastole,
      IntegerProperty dm_posterior_wall_diastole) {
    boolean isValidate =
        dm_left_ventricle_diastole.asObject().get() != null
            && dm_iv_septum_diastole.asObject().get() != null
            && dm_posterior_wall_diastole.asObject().get() != null
            && dm_left_ventricle_diastole.asObject().get() != 0
            && dm_iv_septum_diastole.asObject().get() != 0
            && dm_posterior_wall_diastole.asObject().get() != 0;
    return isValidate;
  }

  public Double calcRwt(
      IntegerProperty dm_posterior_wall_diastole, IntegerProperty dm_left_ventricle_diastole) {
    if (validateRwtInputs(dm_posterior_wall_diastole, dm_left_ventricle_diastole)) {
      logger.debug("Start calculating RWT...");
      Double v =
          calculator.calcRelativeWallThicknessRwt(
              dm_posterior_wall_diastole.asObject().get(),
              dm_left_ventricle_diastole.asObject().get());
      return v;
    } else {
      logger.debug("one or more required field is null");
      return (double) 0;
    }
  }

  private boolean validateRwtInputs(
      IntegerProperty dm_posterior_wall_diastole, IntegerProperty dm_left_ventricle_diastole) {
    return dm_left_ventricle_diastole.asObject().get() != null
        && dm_posterior_wall_diastole.asObject().get() != null
        && dm_left_ventricle_diastole.asObject().get() != 0
        && dm_posterior_wall_diastole.asObject().get() != 0;
  }

  public Double calcLvmIndex(
      IntegerProperty dm_left_ventricle_diastole,
      IntegerProperty dm_iv_septum_diastole,
      IntegerProperty dm_posterior_wall_diastole) {
    if (validateLvmIndexInputs(
        dm_posterior_wall_diastole, dm_iv_septum_diastole, dm_left_ventricle_diastole)) {
      logger.debug("Start calculating RWT...");
      Double v =
          calculator.calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea(
              dm_left_ventricle_diastole.get(),
              dm_iv_septum_diastole.get(),
              dm_posterior_wall_diastole.get(),
              HEIGHT_IN_CM,
              WEIGHT_IN_KG);
      return v;
    } else {
      logger.debug("one or more required field is null");
      return (double) 0;
    }
  }

  private boolean validateLvmIndexInputs(
      IntegerProperty dm_left_ventricle_diastole,
      IntegerProperty dm_iv_septum_diastole,
      IntegerProperty dm_posterior_wall_diastole) {
    return dm_left_ventricle_diastole.asObject().get() != null
        && dm_iv_septum_diastole.asObject().get() != null
        && dm_posterior_wall_diastole.asObject().get() != null
        && dm_left_ventricle_diastole.asObject().get() != 0
        && dm_iv_septum_diastole.asObject().get() != 0
        && dm_posterior_wall_diastole.asObject().get() != 0;
  }
}
