package net.wilamowski.drecho.shared.calcs;

import net.wilamowski.drecho.standalone.domain.echo.EchoCardioCalculator;
import net.wilamowski.drecho.standalone.domain.echo.EchoCardioCalculatorExpressionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EchoCardioCalculatorTest {

  private final EchoCardioCalculator calculator = new EchoCardioCalculatorExpressionImpl();

  @Test
  void calculateBodySurfaceArea_ValidInput_ExpectedResult() {
    // Given
    double heightInCm = 190;
    double weightInKg = 82;

    // When
    double bodySurfaceArea = calculator.calcBodySurfaceArea(heightInCm, weightInKg);

    // Then
    Assertions.assertEquals(2.08, bodySurfaceArea, 0.01);
  }

  @Test
  void calculateLeftVentricularMass_ValidInput_ExpectedResult() {
    // Given
    double lengthInCm = 10;
    double widthInCm = 6;
    double thicknessInCm = 1;

    // When
    double leftVentricularMass =
        calculator.calcLeftVentricularMass(lengthInCm, widthInCm, thicknessInCm);

    // Then
    Assertions.assertEquals(3.85, leftVentricularMass, 0.01);
  }

  @Test
  void calculateLeftVentricularMassIndex_ValidInput_ExpectedResult() {
    // Given
    double lengthInCm = 10;
    double widthInCm = 6;
    double thicknessInCm = 1;
    double heightInCm = 190;
    double weightInKg = 82;

    // When
    double leftVentricularMassIndex =
        calculator.calcLeftVentricularEndDiastolicDimensionMassIndexedToBodySurfaceArea(
            lengthInCm, widthInCm, thicknessInCm, heightInCm, weightInKg);

    // Then
    Assertions.assertEquals(1.85, leftVentricularMassIndex, 0.01);
  }
}
