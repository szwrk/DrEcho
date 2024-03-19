package net.wilamowski.drecho.connectors.model.standalone.domain.calcs;

public interface CardioCalculator {

  /**
   * LVMI calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea Property key:
   * examination.echotte.lvmi.expression
   */
  double calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea(
      double leftVentricularEndDiastolicDimension,
      double interventricularSeptumThickness,
      double posteriorWallThickness,
      double heightInCm,
      double weightInKg);

  /** LVM calcLeftVentricularMass Property key: examination.echotte.lvm.expression */
  double calcLeftVentricularMass(
      double leftVentricularEndDiastolicDimen,
      double interventricularSeptumThickness,
      double posteriorWallThickness);

  /** BSA calcBodySurfaceArea Property key: examination.echotte.bsa.expression */
  double calcBodySurfaceArea(double heightInCm, double weightInKg) throws IllegalArgumentException;

  /** RWT calcRelativeWallThicknessRwt Property key: examination.echotte.rwt.expression */
  Double calcRelativeWallThicknessRwt(Integer integer, Integer integer1)
      throws IllegalArgumentException;
}
