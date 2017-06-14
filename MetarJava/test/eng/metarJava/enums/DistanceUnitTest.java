package eng.metarJava.enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class DistanceUnitTest {
  
  public DistanceUnitTest() {
  }

  @Test
  public void testConvert_M_KM() {
    DistanceUnit sourceUnit = DistanceUnit.meters;
    double sourceValue = 1000;
    DistanceUnit targetUnit = DistanceUnit.kilometers;
    double expectedValue = 1;
    double actualValue = DistanceUnit.convert(sourceValue, sourceUnit, targetUnit);
    assertEquals(expectedValue, actualValue, 0.0);
  }
  
    @Test
  public void testConvert_KM_SM() {
    DistanceUnit sourceUnit = DistanceUnit.kilometers;
    double sourceValue = 250;
    DistanceUnit targetUnit = DistanceUnit.miles;
    double expectedValue = 155.34221891;
    double actualValue = DistanceUnit.convert(sourceValue, sourceUnit, targetUnit);
    assertEquals(expectedValue, actualValue, 1e-7);
  }
  
}
