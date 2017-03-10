package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.CloudMass;
import eng.metarJava.support.Heading;
import eng.metarJava.support.PhenomenaDescriptor;
import eng.metarJava.support.PhenomenaIntensity;
import eng.metarJava.support.PhenomenaType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class GenericFormatTest {

  private static final String LKMT
          = "METAR LKMT 241812Z 02032KT 5000NDV RA +SN BLSNVC SCT060 12/05 Q0996 WS R22 "
          + "SNOCLO NOSIG";
  private static final String LKPD
          = "METAR LKPD 241812Z 02032KT 030V080 CAVOK VV030 12/05 Q1002 RETSRA NOSIG";
  private static final String LKTB
          = "METAR LKTB 241812Z /////KT CAVOK NSC M08/M12 Q1002 NOSIG";
  private static final String LKPR
          = "METAR COR LKPR 312345Z 02012G25KT 2000 0800E R06/0700 R24C/0200V0500"
          + "+TSSQVC FEW012 SCT030CB OVC050TCU 12/05 Q1002 RETSRA RESA WS ALL RWY "
          + "R24/589999 R06/2039// NOSIG";
  private static final String XXYY
          = "METAR COR XXYY 312345Z NIL";
  private static final String UUEE
          = "METAR COR UUEE 312345Z 35037G41MPS CAVOK NCD 12/05 Q1002 NOSIG";

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
    Report r = p.parse(XXYY);

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
    Report r = p.parse(LKTB);

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
    Report r = p.parse(LKPD);
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
    Report r = p.parse(LKPD);
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

  @Test
  public void testParsePhenomenaA() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);

    assertEquals(3, r.getPhenomenas().size());

    PhenomenaInfo pi = r.getPhenomenas().get(0);
    assertEquals(PhenomenaIntensity.moderate, pi.getIntensity());
    assertEquals(PhenomenaDescriptor.none, pi.getDescriptor());
    assertEquals(PhenomenaType.RA, pi.getType());
    assertFalse(pi.isInVicinity());

    pi = r.getPhenomenas().get(1);
    assertEquals(PhenomenaIntensity.heavy, pi.getIntensity());
    assertEquals(PhenomenaDescriptor.none, pi.getDescriptor());
    assertEquals(PhenomenaType.SN, pi.getType());
    assertFalse(pi.isInVicinity());

    pi = r.getPhenomenas().get(2);
    assertEquals(PhenomenaIntensity.moderate, pi.getIntensity());
    assertEquals(PhenomenaDescriptor.BL, pi.getDescriptor());
    assertEquals(PhenomenaType.SN, pi.getType());
    assertTrue(pi.isInVicinity());
  }

  @Test
  public void testParsePhenomenaB() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);

    assertEquals(1, r.getPhenomenas().size());

    PhenomenaInfo pi = r.getPhenomenas().get(0);
    assertEquals(PhenomenaIntensity.heavy, pi.getIntensity());
    assertEquals(PhenomenaDescriptor.TS, pi.getDescriptor());
    assertEquals(PhenomenaType.SQ, pi.getType());
    assertTrue(pi.isInVicinity());
  }

  @Test
  public void testParsePhenomenaC() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPD);

    assertNotNull(r.getPhenomenas());
    assertEquals(0, r.getPhenomenas().size());
  }

  @Test
  public void testParseCloudsVV() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPD);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertTrue(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertFalse(ci.isNSC());
    assertFalse(ci.isSpecialState());
    assertNull(ci.getMasses());

    assertEquals((Integer) 30, ci.getVerticalVisibilityInHundredFeet());
    assertEquals(3000, ci.getVerticalVisibilityInFeet());
  }

  @Test
  public void testParseCloudsNSC() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKTB);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertTrue(ci.isNSC());
    assertTrue(ci.isSpecialState());
    assertNull(ci.getMasses());
  }

  @Test
  public void testParseCloudsNCD() {
    IParse p = new GenericFormat();
    Report r = p.parse(UUEE);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertTrue(ci.isNCD());
    assertFalse(ci.isNSC());
    assertTrue(ci.isSpecialState());
    assertNull(ci.getMasses());
  }

  @Test
  public void testParseCloudsMass1() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertFalse(ci.isNSC());
    assertFalse(ci.isSpecialState());
    assertNotNull(ci.getMasses());
    assertEquals(1, ci.getMasses().size());
    CloudMass cm = ci.getMasses().get(0);
    assertEquals(CloudAmount.SCT, cm.getAmount());
    assertEquals(60, cm.getBaseHeightHundredFeet());
    assertEquals(CloudMassSignificantFlag.none, cm.getSignificantFlag());
  }

  @Test
  public void testParseCloudsMass3() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertFalse(ci.isNSC());
    assertFalse(ci.isSpecialState());
    assertNotNull(ci.getMasses());
    assertEquals(3, ci.getMasses().size());

    CloudMass cm;
    cm = ci.getMasses().get(0);
    assertEquals(CloudAmount.FEW, cm.getAmount());
    assertEquals(12, cm.getBaseHeightHundredFeet());
    assertEquals(CloudMassSignificantFlag.none, cm.getSignificantFlag());

    cm = ci.getMasses().get(1);
    assertEquals(CloudAmount.SCT, cm.getAmount());
    assertEquals(30, cm.getBaseHeightHundredFeet());
    assertEquals(CloudMassSignificantFlag.CB, cm.getSignificantFlag());

    cm = ci.getMasses().get(2);
    assertEquals(CloudAmount.OVC, cm.getAmount());
    assertEquals(50, cm.getBaseHeightHundredFeet());
    assertEquals(CloudMassSignificantFlag.TCU, cm.getSignificantFlag());
  }

  @Test
  public void testParseTemperatureAndDewPoint() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);

    assertEquals(12, r.getTemperature());
    assertEquals(5, r.getDewPoint());
  }

  @Test
  public void testParseTemperatureAndDewPointMinus() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKTB);

    assertEquals(-8, r.getTemperature());
    assertEquals(-12, r.getDewPoint());
  }

  @Test
  public void testParsePressure() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);

    assertEquals(996, r.getPressureInHpa());
  }

  @Test
  public void testParseRecentPhenomena() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPD);
    PhenomenaInfo pi;

    assertEquals(1, r.getRecentPhenomenas().size());
    pi = r.getRecentPhenomenas().get(0);
    assertEquals(PhenomenaDescriptor.TS, pi.getDescriptor());
    assertEquals(PhenomenaType.RA, pi.getType());
  }

  @Test
  public void testParseRecentPhenomena2() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);
    PhenomenaInfo pi;

    assertEquals(2, r.getRecentPhenomenas().size());
    pi = r.getRecentPhenomenas().get(0);
    assertEquals(PhenomenaDescriptor.TS, pi.getDescriptor());
    assertEquals(PhenomenaType.RA, pi.getType());

    pi = r.getRecentPhenomenas().get(1);
    assertEquals(PhenomenaDescriptor.none, pi.getDescriptor());
    assertEquals(PhenomenaType.SA, pi.getType());
  }

  @Test
  public void testParseWindshearAll() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);

    assertNotNull(r.getRunwayWindshears());
    assertNull(r.getRunwayWindshears().getRunwayDesignators());
    assertTrue(r.getRunwayWindshears().isAllRunways());
  }

  @Test
  public void testParseWindshear() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);

    assertNotNull(r.getRunwayWindshears());
    assertEquals(1, r.getRunwayWindshears().getRunwayDesignators().size());
    assertFalse(r.getRunwayWindshears().isAllRunways());
    for (String runwayDesignator : r.getRunwayWindshears().getRunwayDesignators()) {
      assertEquals("22", runwayDesignator);
    }
  }

  @Test
  public void testParseRunwayStateSNOCLO() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKMT);

    assertNotNull(r.getRunwayStateInfo());
    assertNull(r.getRunwayStateInfo().getRunwayStates());
    assertTrue(r.getRunwayStateInfo().isSnowClosed());
  }

  @Test
  public void testParseRunwayState2() {
    IParse p = new GenericFormat();
    Report r = p.parse(LKPR);

    assertNotNull(r.getRunwayStateInfo());
    assertFalse(r.getRunwayStateInfo().isEmpty());
    assertFalse(r.getRunwayStateInfo().isSnowClosed());
    assertEquals(2, r.getRunwayStateInfo().getRunwayStates().size());

    RunwayState rs;
    rs = r.getRunwayStateInfo().getRunwayStates().get(0);
    assertEquals("24", rs.getDesignator());
    assertEquals('5', rs.getDeposit());
    assertEquals('8', rs.getContamination());
    assertEquals("99", rs.getDepositDepth());
    assertEquals("99", rs.getBrakingAction());
    
    rs = r.getRunwayStateInfo().getRunwayStates().get(1);
    assertEquals("06", rs.getDesignator());
    assertEquals('2', rs.getDeposit());
    assertEquals('0', rs.getContamination());
    assertEquals("39", rs.getDepositDepth());
    assertEquals("//", rs.getBrakingAction());
  }
}
