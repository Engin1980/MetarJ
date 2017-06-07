package eng.metarJava.decoders;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.Speed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Marek Vajgl
 */
public class EAssert {
  public static void assertEqualsSpeed(int expected, Speed speed, SpeedUnit unit){
    assertEquals(expected, speed.getIntValue(unit));
  }

  public static void assertStringStarts(String expected, String actual){
    String actualPart;
    if (expected.length() >= actual.length())
      actualPart = actual;
    else
      actualPart = actual.substring(0, expected.length());
    assertEquals(expected, actualPart);
  }
}
