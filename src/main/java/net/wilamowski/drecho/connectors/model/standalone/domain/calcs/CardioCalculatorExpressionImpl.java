package net.wilamowski.drecho.connectors.model.standalone.domain.calcs;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.properties.BackendPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardioCalculatorExpressionImpl implements CardioCalculator {
  public static final String PATTERN_FORMAT_DOUBLE = "#.##";
  public static final int BODY_SURFACE_AREA_FACTOR = 3600;
  public static final double HEART_MUSCLE_DENSITY = 1.04;
  private static final Logger logger = LogManager.getLogger( CardioCalculatorExpressionImpl.class);

  @Override
  public double calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea(
      double leftVentricularEndDiastolicDimension,
      double interventricularSeptumThickness,
      double posteriorWallThickness,
      double heightInCm,
      double weightInKg) {

    String expreesion = ClientPropertyReader.getString("examination.echotte.lvmi.expression");

    Expression expression =
        new ExpressionBuilder(expreesion)
            .variables("bsa", "lvmass")
            .build()
            .setVariable(
                "lvmass",
                calcLeftVentricularMass(
                    leftVentricularEndDiastolicDimension,
                    interventricularSeptumThickness,
                    posteriorWallThickness))
            .setVariable("bsa", calcBodySurfaceArea(heightInCm, weightInKg));
    double evaluate = expression.evaluate();
    double format = format(evaluate);
    logger.debug("Calculate result: {}, {}", evaluate, format);
    return format;
  }

  @Override
  public double calcLeftVentricularMass(
      double leftVentricularEndDiastolicDimenMm,
      double interventricularSeptumThicknessMm,
      double posteriorWallThicknessMm) {

    double leftVentricularEndDiastolicDimenCm = convertMmToCm(leftVentricularEndDiastolicDimenMm);
    double interventricularSeptumThicknessCm = convertMmToCm(interventricularSeptumThicknessMm);
    double posteriorWallThicknessCm = convertMmToCm(posteriorWallThicknessMm);

    String expreesion = "1.04*(pow(lvedd+pwd+ivsd,3)-pow(lvedd,3))*0.8+0.6";

    Expression expression =
        new ExpressionBuilder(expreesion)
            .variables("lvedd", "ivsd", "pwd")
            .build()
            .setVariable("lvedd", leftVentricularEndDiastolicDimenCm)
            .setVariable("ivsd", interventricularSeptumThicknessCm)
            .setVariable("pwd", posteriorWallThicknessCm);
    double evaluate = expression.evaluate();
    logger.debug("Calculate result: {}, {}", evaluate, format(evaluate));
    return format(evaluate);
  }

  @Override
  public double calcBodySurfaceArea(double heightInCm, double weightInKg)
      throws IllegalArgumentException {
    String expreesion = BackendPropertyReader.getString("examination.echotte.bsa.expression");
    Expression expression =
        new ExpressionBuilder(expreesion)
            .variables("heightInCm", "weightInKg", "factor")
            .build()
            .setVariable("heightInCm", heightInCm)
            .setVariable("weightInKg", weightInKg)
            .setVariable("factor", BODY_SURFACE_AREA_FACTOR);
    if (expression.validate().isValid()) {
      double evaluate = expression.evaluate();
      logger.debug(
          "Calculate result: {}, {}. Errors: {}",
          evaluate,
          format(evaluate),
          expression.validate().getErrors());
      return format(expression.evaluate());
    } else {
      String s = "BSA expression is not valid";
      logger.error(s);
      throw new IllegalArgumentException(s);
    }
  }

  @Override
  public Double calcRelativeWallThicknessRwt(
      Integer posteriorWallDuringDiastoleThickness,
      Integer leftVentricleDuringDiastoleInternalDiameter)
      throws IllegalArgumentException {
    String expreesion = BackendPropertyReader.getString("examination.echotte.rwt.expression");
    Expression expression =
        new ExpressionBuilder(expreesion)
            .variables("pwtd", "lvedd")
            .build()
            .setVariable("pwtd", posteriorWallDuringDiastoleThickness)
            .setVariable("lvedd", leftVentricleDuringDiastoleInternalDiameter);

    if (expression.validate().isValid()) {
      double evaluate = expression.evaluate();
      logger.debug(
          "Calculate result: {}, {}. Errors: {}",
          evaluate,
          format(evaluate),
          expression.validate().getErrors());
      return format(evaluate);
    } else {
      String s = "RWT expression is not valid";
      logger.error(s);
      throw new IllegalArgumentException(s);
    }
  }

  private double format(double value) {
    double result = 0;
    try {
      DecimalFormat df = new DecimalFormat(PATTERN_FORMAT_DOUBLE);
      df.setRoundingMode(RoundingMode.FLOOR);
      result = Double.parseDouble(df.format(value));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return result;
  }

  private static double convertMmToCm(double valueMm) {
    return valueMm / 10;
  }
}
