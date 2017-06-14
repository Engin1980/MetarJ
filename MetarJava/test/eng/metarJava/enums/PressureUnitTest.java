package eng.metarJava.enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class PressureUnitTest {
  
  @Test
  public void testConvert() {
        PressureUnit sourceUnit = PressureUnit.hpa;
    double sourceValue = 1013;
    PressureUnit targetUnit = PressureUnit.inHq;
    double expectedValue = 29.92;
    double actualValue = PressureUnit.convert(sourceValue, sourceUnit, targetUnit);
    assertEquals(expectedValue, actualValue, 3e-1);
  }
  
  @Test
  public void testConvert2() {
        PressureUnit sourceUnit = PressureUnit.inHq;
    double sourceValue = 29.92;
    PressureUnit targetUnit = PressureUnit.hpa;
    double expectedValue = 1013;
    double actualValue = PressureUnit.convert(sourceValue, sourceUnit, targetUnit);
    assertEquals(expectedValue, actualValue, 3e-1);
  }
  
    @Test
  public void testConvert3() {
        PressureUnit sourceUnit = PressureUnit.inHq;
    double sourceValue = 30.11;
    PressureUnit targetUnit = PressureUnit.hpa;
    double expectedValue = 1019.;
    double actualValue = PressureUnit.convert(sourceValue, sourceUnit, targetUnit);
    assertEquals(expectedValue, actualValue, 5e-1);
  }
  
}
