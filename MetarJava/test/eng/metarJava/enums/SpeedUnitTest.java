package eng.metarJava.enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class SpeedUnitTest {

  @Test
  public void testConvert_Kmh_Kmh() {
    Double in = 10d;
    SpeedUnit inUnit = SpeedUnit.KMH;
    SpeedUnit outUnit = SpeedUnit.KMH;
    Double expected = 10d;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNotNull(actual);
    assertEquals(expected, actual, 1e-3);
  }
  
  @Test
  public void testConvert_Kmh_Kt() {
    Double in = 100d;
    SpeedUnit inUnit = SpeedUnit.KMH;
    SpeedUnit outUnit = SpeedUnit.KT;
    Double expected = 53.99568d;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNotNull(actual);
    assertEquals(expected, actual, 1e-3);
  }
  
    @Test
  public void testConvert_Kmh_Mps() {
    Double in = 100d;
    SpeedUnit inUnit = SpeedUnit.KMH;
    SpeedUnit outUnit = SpeedUnit.MPS;
    Double expected = 27.7777777777d;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNotNull(actual);
    assertEquals(expected, actual, 1e-3);
  }
  
      @Test
  public void testConvert_Mps_Kmh() {
    Double in = 100d;
    SpeedUnit inUnit = SpeedUnit.MPS;
    SpeedUnit outUnit = SpeedUnit.KMH;
    Double expected = 360d;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNotNull(actual);
    assertEquals(expected, actual, 1e-3);
  }
  
  @Test
  public void testConvert_Mps_Kt() {
    Double in = 100d;
    SpeedUnit inUnit = SpeedUnit.MPS;
    SpeedUnit outUnit = SpeedUnit.KT;
    Double expected = 194.38444924574d;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNotNull(actual);
    assertEquals(expected, actual, 1e-3);
  }
  
  @Test
  public void testConvert_Mps_NULL() {
    Double in = null;
    SpeedUnit inUnit = SpeedUnit.MPS;
    SpeedUnit outUnit = SpeedUnit.KT;
    Double actual = SpeedUnit.convert(in, inUnit, outUnit);
    assertNull(actual);
  }
  
  

}
