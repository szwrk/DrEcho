package net.wilamowski.drecho.connectors.model.standalone.domain.calcs;

import static java.lang.Math.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * https://www.mdapp.co/lv-mass-index-calculator-478/ Left Ventricular Mass Index Explained Left
 * ventricular mass is an important determinant of diagnosis and prognosis in patients with heart
 * disease (cardiovascular morbidity and mortality) in specific for determination of severity and
 * type of cardiac hypertrophy.
 *
 * <p>The parameters involved are summarised below:
 *
 * <p>LVEDD: Left ventricular end-diastolic dimension; IVSd: Interventricular septal thickness at
 * end-diastole; PWd: Posterior wall thickness at end-diastole; LVMI: Left ventricular mass index;
 * RWT: Relative wall thickness; BSA: Body surface area using the Mosteller formula. The formulas
 * used are the following:
 *
 * <p>LV Mass = 0.8 x (1.04 x (((LVEDD + IVSd +PWd)3 - LVEDD3))) + 0.6 LVMI (LV Mass Indexed to Body
 * Surface Area) = LV Mass / BSA RWT (Relative Wall Thickness) = 2 x PWd / LVEDD For BSA,
 * Mosteller’s formula is employed: BSA = (((Height in cm) x (Weight in kg))/ 3600)½
 */

@Deprecated
public class CardioCalculatorLvmi implements CardioCalculator {
  public static final int BODY_SURFACE_AREA_FACTOR = 3600;
  public static final double MULTIPLIER_0_8 = 0.8;
  public static final double Heart_muscle_density = 1.04;
  public static final double POW_3 = 3;
  public static final double ADD_0_6G = 0.6;
  public static final String PATTERN_FORMAT_DOUBLE = "#.##";
  private static final Logger logger = LogManager.getLogger( CardioCalculatorLvmi.class);

  public CardioCalculatorLvmi() {
    throw new NotImplementedException( "CalculatorLvmi is deprecated" );
  }

  @Override
  public double calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea(
          double leftVentricularEndDiastolicDimension ,
          double interventricularSeptumThickness ,
          double posteriorWallThickness ,
          double heightInCm ,
          double weightInKg) {
    double lvMass =
        calcLeftVentricularMass(
            leftVentricularEndDiastolicDimension,
            interventricularSeptumThickness,
            posteriorWallThickness);
    double bsa = calcBodySurfaceArea(heightInCm, weightInKg);
    double lvMassIndex = lvMass / bsa;
    logger.debug("LVMI: {} / {} = {}", lvMass, bsa, lvMassIndex);
    return format( lvMassIndex );
  }
  @Override
  public double calcLeftVentricularMass(
          double leftVentricularEndDiastolicDimen ,
          double interventricularSeptumThickness ,
          double posteriorWallThickness) {

    double lveddMm = convertMmToCm(leftVentricularEndDiastolicDimen);
    double ivsdMm = convertMmToCm(interventricularSeptumThickness);
    double pwdMm = convertMmToCm(posteriorWallThickness);

    double dimensionSum = sumDimenensions(lveddMm, ivsdMm, pwdMm);

    logCalculating(lveddMm, dimensionSum);
    return format(MULTIPLIER_0_8
            * (Heart_muscle_density * (thirdPower(dimensionSum) - thirdPower(lveddMm))))
        + ADD_0_6G;
  }

  private static double sumDimenensions(
      double leftVentricularEndDiastolicDimen,
      double interventricularSeptumThickness,
      double posteriorWallThickness) {
      return leftVentricularEndDiastolicDimen + interventricularSeptumThickness + posteriorWallThickness;
  }

  private static double convertMmToCm(double valueMm) {
    return valueMm / 10;
  }

  @Override
  public double calcBodySurfaceArea(double heightInCm , double weightInKg) {
    double result = sqrt((heightInCm * weightInKg) / BODY_SURFACE_AREA_FACTOR);
    logger.debug("BSA: {} ", result);
    return result;
  }

  @Override
  public Double calcRelativeWallThicknessRwt(Integer integer , Integer integer1) {
    return null;
  }

  private double format(double value){
    double result = 0;
    try {
    DecimalFormat df = new DecimalFormat( PATTERN_FORMAT_DOUBLE );
    df.setRoundingMode( RoundingMode.CEILING);
    result = Double.parseDouble( df.format( value ) );
    } catch (Exception e){
      logger.error( e.getMessage(), e );
    }
    return result;
  }

  private static void logCalculating(double leftVentricularEndDiastolicDimen, double dimensionSum) {
    logger.debug(
            "{} * ( {} * {} - {} ) + {} = {}",
            MULTIPLIER_0_8,
            Heart_muscle_density,
            thirdPower(dimensionSum),
            thirdPower(leftVentricularEndDiastolicDimen),
            ADD_0_6G ,
            MULTIPLIER_0_8
                    * (Heart_muscle_density * thirdPower(dimensionSum)
                    - thirdPower(leftVentricularEndDiastolicDimen))
                    + ADD_0_6G );

    logger.debug(
            "{} * {} + {} = {}",
            MULTIPLIER_0_8,
            (Heart_muscle_density * thirdPower(dimensionSum)
                    - thirdPower(leftVentricularEndDiastolicDimen)),
            ADD_0_6G ,
            MULTIPLIER_0_8
                    * (Heart_muscle_density * thirdPower(dimensionSum)
                    - thirdPower(leftVentricularEndDiastolicDimen))
                    + ADD_0_6G );
  }

  private static double thirdPower(double leftVentricularEndDiastolicDimen) {
    return pow(leftVentricularEndDiastolicDimen, POW_3);
  }


}
