package eng.metarJava.decoders;

import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.Direction;
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

  private static final String LKMT = "METAR LKMT 241812Z 02032KT 5000NDV";
  private static final String LKMT_VARIATING_WIND = "METAR LKMT 241812Z 02032KT 030V080 CAVOK";
  private static final String LKMT_NO_WIND = "METAR LKMT 241812Z /////KT CAVOK";
  private static final String LKPR = "METAR COR LKPR 312345Z 02012G25KT 2000 0800E R06/0700 R24C/0200V0500";
  private static final String LKPR_NIL = "METAR COR LKPR 312345Z NIL";
  private static final String UUEE = "METAR COR UUEE 312345Z 35037G41MPS CAVOK";

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
    assertFalse(w.isVariating());
  }

  @Test
  public void testParseWindGust() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    WindInfo w = r.getWind();

    assertNotNull(w);
    assertEquals(new Heading(20), w.getDirection());
    assertEquals(12, w.getSpeed(SpeedUnit.KT));
    assertNotNull(w.tryGetGustingSpeed(SpeedUnit.KT));
    assertEquals(25, w.getGustingSpeed(SpeedUnit.KT));
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

  @Test
  public void testParseWindVariating() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT_VARIATING_WIND);
    WindInfo w = r.getWind();

    assertNotNull(w);
    assertTrue(w.isVariating());
    assertNotNull(w.getVariation());
    assertEquals(new Heading(30), w.getVariation().getFrom());
    assertEquals(new Heading(80), w.getVariation().getTo());
  }

  @Test
  public void testParseVisibilityCavok() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT_VARIATING_WIND);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertTrue(v.isCaVOk());
    assertFalse(v.isNoDirectionalVisibility());
    assertFalse(v.isVariating());
  }

  @Test
  public void testParseVisibilityValueAndNDV() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertFalse(v.isCaVOk());
    assertTrue(v.isNoDirectionalVisibility());
    assertNotNull(v.getVisibilityInMeters());
    assertEquals(5000, (int) v.getVisibilityInMeters());
    assertFalse(v.isVariating());
  }

  @Test
  public void testParseVisibilityVariating() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertFalse(v.isCaVOk());
    assertFalse(v.isNoDirectionalVisibility());
    assertNotNull(v.getVisibilityInMeters());
    assertEquals(2000, (int) v.getVisibilityInMeters());
    assertTrue(v.isVariating());
    assertEquals(800, v.getVariability().getVisibilityInMeters());
    assertEquals(Direction.east, v.getVariability().getDirection());
  }
  
  @Test
  public void testParseRunwayVisualRange() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    RunwayVisualRange rvr;
    
    assertEquals(2, r.getRunwayVisualRanges().size());
    
    rvr = r.getRunwayVisualRanges().get(0);
    assertFalse(rvr.isVariating());
    assertEquals("06", rvr.getRunwayDesignator());
    assertNotNull(rvr.getVisibilityInMeters());
    assertNull(rvr.getVariatingVisibilityInMeters());
    assertEquals(700, (int) rvr.getVisibilityInMeters());
    
    rvr = r.getRunwayVisualRanges().get(1);
    assertTrue(rvr.isVariating());
    assertEquals("24C", rvr.getRunwayDesignator());
    assertNull(rvr.getVisibilityInMeters());
    assertNotNull(rvr.getVariatingVisibilityInMeters());
    assertEquals(200, (int) rvr.getVariatingVisibilityInMeters().getFrom());
    assertEquals(500, (int) rvr.getVariatingVisibilityInMeters().getTo());
  }
}
