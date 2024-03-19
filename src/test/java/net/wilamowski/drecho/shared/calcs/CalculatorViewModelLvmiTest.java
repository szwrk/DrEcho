package net.wilamowski.drecho.shared.calcs;

import net.wilamowski.drecho.connectors.model.standalone.domain.calcs.CardioCalculator;
import net.wilamowski.drecho.connectors.model.standalone.domain.calcs.CardioCalculatorExpressionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardioCalculatorTest {

  private final CardioCalculator calculator = new CardioCalculatorExpressionImpl();

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
