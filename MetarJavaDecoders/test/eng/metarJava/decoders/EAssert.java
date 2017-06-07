package eng.metarJava.decoders;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.Speed;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Marek Vajgl
 */
public class EAssert {
  public static void assertEqualsSpeed(int expected, Speed speed, SpeedUnit unit){
    assertEquals(expected, speed.getIntValue(unit));
  }
}
