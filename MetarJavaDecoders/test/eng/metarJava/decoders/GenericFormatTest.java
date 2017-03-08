package eng.metarJava.decoders;

import eng.metarJava.Report;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.Heading;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class GenericFormatTest {
  
  private static final String LKMT = "METAR LKMT 241812Z 02032KT";
  private static final String LKMT_NO_WIND = "METAR LKMT 241812Z /////KT";
  private static final String LKPR = "METAR COR LKPR 312345Z 02032G13KT";
  private static final String LKPR_NIL = "METAR COR LKPR 312345Z NIL";
  private static final String UUEE = "METAR COR UUEE 312345Z 35037G41MPS";

  @Test
  public void testParseReportType() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);
    
    ReportType exp = ReportType.METAR;
    ReportType act = r.getType();
    
    assertEquals(exp, act);
  }
  
  @Test
  public void testParseCor() {
    IParse p = new GenericFormat();
    Report r; 
    
    r = p.parse(LKPR);
    assertTrue(r.isCorrection());
    
    r = p.parse(LKMT);
    assertFalse(r.isCorrection());
  }
  
  @Test
  public void testParseIcao() {
    IParse p = new GenericFormat();
    Report r; 
    
    r = p.parse(LKPR);
    assertEquals("LKPR", r.getIcao());
    
    r = p.parse(LKMT);
    assertEquals("LKMT", r.getIcao());
  }
  
  @Test
  public void testParseDayTime() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    
    assertNotNull(r.getDayTime());
    assertEquals(31, r.getDayTime().getDay());
    assertEquals(23, r.getDayTime().getHour());
    assertEquals(45, r.getDayTime().getMinute());
  }
  
  @Test
  public void testParseNil() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR_NIL);
    
    assertTrue(r.isNil());
  }
  
  @Test
  public void testParseNilFalse() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    
    assertFalse(r.isNil());
  }
  
  @Test
  public void testParseWindUnset() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT_NO_WIND);
    
    assertNull(r.getWind());
  }
  
  @Test
  public void testParseWind() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);
    WindInfo w = r.getWind();
    
    assertNotNull(w);
    assertEquals(new Heading(20), w.getDirection());
    assertEquals(32, w.getSpeed(SpeedUnit.KT));
    assertNull(w.tryGetGustingSpeed(SpeedUnit.KT));
  }
  
  @Test
  public void testParseWindGust() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    WindInfo w = r.getWind();
    
    assertNotNull(w);
    assertEquals(new Heading(20), w.getDirection());
    assertEquals(32, w.getSpeed(SpeedUnit.KT));
    assertNotNull(w.tryGetGustingSpeed(SpeedUnit.KT));
    assertEquals(13, w.getGustingSpeed(SpeedUnit.KT));
  }
  
  @Test
  public void testParseWindGustAndMps() {
    IParse p = new GenericFormat();
    Report r = p.parse(UUEE);
    WindInfo w = r.getWind();
    
    assertNotNull(w);
    assertEquals(new Heading(350), w.getDirection());
    assertEquals(37, w.getSpeed(SpeedUnit.MPS));
    assertNotNull(w.tryGetGustingSpeed(SpeedUnit.KT));
    assertEquals(41, w.getGustingSpeed(SpeedUnit.MPS));
  }
}
