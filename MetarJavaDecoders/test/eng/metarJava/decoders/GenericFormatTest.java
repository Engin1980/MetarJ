package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.TrendReport;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.enums.TrendReportType;
import eng.metarJava.CloudMass;
import eng.metarJava.support.Heading;
import eng.metarJava.enums.PhenomenaIntensity;
import eng.metarJava.enums.PhenomenaType;
import org.junit.Test;
import static org.junit.Assert.*;
import static eng.metarJava.decoders.EAssert.*;

/**
 *
 * @author Marek Vajgl
 */
public class GenericFormatTest {

  private static final String LKMT
          = "METAR LKMT 241812Z 02032KT 5000NDV RA +SN +BLSNVC SCT060 12/05 Q0996 WS R22 "
          + "SNOCLO BECMG AT1900 01015KT CAVOK NSW NSC";
  private static final String LKPD
          = "METAR LKPD 241812Z 02032KT 030V080 CAVOK VV030 12/05 Q1002 RETSRA NOSIG";
  private static final String LKTB
          = "METAR LKTB 241812Z /////KT CAVOK NSC M08/M12 Q1002 NOSIG";
  private static final String LKPR
          = "METAR COR LKPR 312345Z 02012G25KT 2000 0800E R06/0700 R24C/0200V0500"
          + "VCTSSQ FEW012 SCT030CB OVC050TCU 12/05 Q1002 RETSRA RESA WS ALL RWY "
          + "R24/589999 R06/2039// "
          + "BECMG AT1900 01015KT 4000 +SN VV300";
  private static final String XXYY
          = "METAR COR XXYY 312345Z NIL";
  private static final String UUEE
          = "METAR COR UUEE 312345Z 35037G41MPS CAVOK NCD 12/05 Q1002 NOSIG";
  
  @Test
  public void testParseReportType() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    ReportType exp = ReportType.METAR;
    ReportType act = r.getType();

    assertEquals(exp, act);
  }

  @Test
  public void testParseCor() {
    Parser p = new EUParser();
    Report r;

    r = p.parse(LKPR);
    assertTrue(r.isCorrection());

    r = p.parse(LKMT);
    assertFalse(r.isCorrection());
  }

  @Test
  public void testParseIcao() {
    Parser p = new EUParser();
    Report r;

    r = p.parse(LKPR);
    assertEquals("LKPR", r.getIcao());

    r = p.parse(LKMT);
    assertEquals("LKMT", r.getIcao());
  }

  @Test
  public void testParseDayTime() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertNotNull(r.getDayTime());
    assertEquals(31, r.getDayTime().getDay());
    assertEquals(23, r.getDayTime().getHour());
    assertEquals(45, r.getDayTime().getMinute());
  }

  @Test
  public void testParseNil() {
    Parser p = new EUParser();
    Report r = p.parse(XXYY);

    assertTrue(r.isNil());
  }

  @Test
  public void testParseNilFalse() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertFalse(r.isNil());
  }

  @Test
  public void testParseWindUnset() {
    Parser p = new EUParser();
    Report r = p.parse(LKTB);

    assertNull(r.getWind());
  }

  @Test
  public void testParseWind() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);
    WindInfo w = r.getWind();

    assertNotNull(w);
    assertEquals(new Heading(20), w.getDirection());
    assertEqualsSpeed(32, w.getSpeed(), SpeedUnit.KT);
    assertFalse(w.isGusting());
    assertFalse(w.isVariating());
  }

  @Test
  public void testParseWindGust() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);
    WindInfo w = r.getWind();

    assertNotNull(w);
    assertEquals(new Heading(20), w.getDirection());
    assertEqualsSpeed(12, w.getSpeed(), SpeedUnit.KT);
    assertTrue(w.isGusting());
    assertEqualsSpeed(25, w.getGustingSpeed(), SpeedUnit.KT);
  }

  @Test
  public void testParseWindGustAndMps() {
    Parser p = new EUParser();
    Report r = p.parse(UUEE);
    WindInfo w = r.getWind();

    assertNotNull(w);
    assertEquals(new Heading(350), w.getDirection());
    assertEqualsSpeed(37, w.getSpeed(), SpeedUnit.MPS);
    assertTrue(w.isGusting());
    assertEqualsSpeed(41, w.getGustingSpeed(), SpeedUnit.MPS);
  }

  @Test
  public void testParseWindVariating() {
    Parser p = new EUParser();
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
    Parser p = new EUParser();
    Report r = p.parse(LKPD);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertTrue(v.isCAVOK());
    assertFalse(v.isNoDirectionalVisibility());
    assertFalse(v.isVariating());
  }

  @Test
  public void testParseVisibilityValueAndNDV() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertFalse(v.isCAVOK());
    assertTrue(v.isNoDirectionalVisibility());
    assertNotNull(v.getVisibilityInMeters());
    assertEquals(5000, (int) v.getVisibilityInMeters());
    assertFalse(v.isVariating());
  }

  @Test
  public void testParseVisibilityVariating() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);
    VisibilityInfo v = r.getVisibility();

    assertNotNull(v);
    assertFalse(v.isCAVOK());
    assertFalse(v.isNoDirectionalVisibility());
    assertNotNull(v.getVisibilityInMeters());
    assertEquals(2000, (int) v.getVisibilityInMeters());
    assertTrue(v.isVariating());
    assertEquals(800, v.getVariability().getVisibilityInMeters());
    assertEquals(Direction.east, v.getVariability().getDirection());
  }

  @Test
  public void testParseRunwayVisualRange() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);
    RunwayVisualRange rvr;

    assertEquals(2, r.getRunwayVisualRanges().size());

    rvr = r.getRunwayVisualRanges().get(0);
    assertFalse(rvr.isVariating());
    assertEquals("06", rvr.getRunwayDesignator());
    assertNotNull(rvr.getVisibilityInMeters());
    assertEquals(700, (int)(double) rvr.getVisibilityInMeters());

    rvr = r.getRunwayVisualRanges().get(1);
    assertTrue(rvr.isVariating());
    assertEquals("24C", rvr.getRunwayDesignator());
    assertNotNull(rvr.getVariatingVisibilityInMeters());
    assertEquals(200, (int)(double) rvr.getVariatingVisibilityInMeters().getFrom());
    assertEquals(500, (int)(double) rvr.getVariatingVisibilityInMeters().getTo());
  }

  @Test
  public void testParsePhenomenaA() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertEquals(3, r.getPhenomenas().size());

    PhenomenaInfo pi = r.getPhenomenas().get(0);
    assertEquals(PhenomenaIntensity.moderate, pi.getIntensity());
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.RA},
            pi.getTypes()
    );
    assertEquals(false, pi.isInVicinity());

    pi = r.getPhenomenas().get(1);
    assertEquals(PhenomenaIntensity.heavy, pi.getIntensity());
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.SN},
            pi.getTypes()
    );
    assertEquals(false, pi.isInVicinity());

    pi = r.getPhenomenas().get(2);
    assertEquals(PhenomenaIntensity.heavy, pi.getIntensity());
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.BL, PhenomenaType.SN},
            pi.getTypes()
    );
    assertEquals(true, pi.isInVicinity());
  }

  @Test
  public void testParsePhenomenaB() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertEquals(1, r.getPhenomenas().size());

    PhenomenaInfo pi = r.getPhenomenas().get(0);
    assertEquals(PhenomenaIntensity.moderate, pi.getIntensity());
    assertEquals(true, pi.isInVicinity());
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.TS, PhenomenaType.SQ},
            pi.getTypes()
    );
    assertEquals(true, pi.isInVicinity());
  }

  @Test
  public void testParsePhenomenaC() {
    Parser p = new EUParser();
    Report r = p.parse(LKPD);

    assertNotNull(r.getPhenomenas());
    assertEquals(0, r.getPhenomenas().size());
  }

  @Test
  public void testParseCloudsVV() {
    Parser p = new EUParser();
    Report r = p.parse(LKPD);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertTrue(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertFalse(ci.isNSC());
    assertFalse(ci.isSpecialState());
    assertFalse(ci.isWithMasses());

    assertEquals(30, (int) ci.getVerticalVisibilityInHundredFeet());
    assertEquals(3000, (int) ci.getVerticalVisibilityInFeet());
  }

  @Test
  public void testParseCloudsNSC() {
    Parser p = new EUParser();
    Report r = p.parse(LKTB);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertFalse(ci.isNCD());
    assertTrue(ci.isNSC());
    assertTrue(ci.isSpecialState());
    assertFalse(ci.isWithMasses());
  }

  @Test
  public void testParseCloudsNCD() {
    Parser p = new EUParser();
    Report r = p.parse(UUEE);
    CloudInfo ci = r.getClouds();

    assertNotNull(ci);
    assertFalse(ci.isVerticalVisibility());
    assertTrue(ci.isNCD());
    assertFalse(ci.isNSC());
    assertTrue(ci.isSpecialState());
    assertFalse(ci.isWithMasses());
  }

  @Test
  public void testParseCloudsMass1() {
    Parser p = new EUParser();
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
    Parser p = new EUParser();
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
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertEquals(12, r.getTemperature());
    assertEquals(5, r.getDewPoint());
  }

  @Test
  public void testParseTemperatureAndDewPointMinus() {
    Parser p = new EUParser();
    Report r = p.parse(LKTB);

    assertEquals(-8, r.getTemperature());
    assertEquals(-12, r.getDewPoint());
  }

  @Test
  public void testParsePressure() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertEquals(996, (int)r.getPressureInHpa());
  }

  @Test
  public void testParseRecentPhenomena() {
    Parser p = new EUParser();
    Report r = p.parse(LKPD);
    PhenomenaInfo pi;

    assertEquals(1, r.getRecentPhenomenas().size());
    pi = r.getRecentPhenomenas().get(0);
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.TS, PhenomenaType.RA},
            pi.getTypes()
    );
  }

  @Test
  public void testParseRecentPhenomena2() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);
    PhenomenaInfo pi;

    assertEquals(2, r.getRecentPhenomenas().size());
    pi = r.getRecentPhenomenas().get(0);
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.TS, PhenomenaType.RA},
            pi.getTypes()
    );

    pi = r.getRecentPhenomenas().get(1);
    assertArrayEquals(
            new PhenomenaType[]{PhenomenaType.SA},
            pi.getTypes()
    );

  }

  @Test
  public void testParseWindshearAll() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertNotNull(r.getRunwayWindshears());
    assertNull(r.getRunwayWindshears().getRunwayDesignators());
    assertTrue(r.getRunwayWindshears().isAllRunways());
  }

  @Test
  public void testParseWindshear() {
    Parser p = new EUParser();
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
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertNotNull(r.getRunwayStatesInfo());
    assertNull(r.getRunwayStatesInfo().getRunwayStates());
    assertTrue(r.getRunwayStatesInfo().isSnowClosed());
  }

  @Test
  public void testParseRunwayState2() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertNotNull(r.getRunwayStatesInfo());
    assertFalse(r.getRunwayStatesInfo().isEmpty());
    assertFalse(r.getRunwayStatesInfo().isSnowClosed());
    assertEquals(2, r.getRunwayStatesInfo().getRunwayStates().size());

    RunwayState rs;
    rs = r.getRunwayStatesInfo().getRunwayStates().get(0);
    assertEquals("24", rs.getDesignator());
    assertEquals('5', rs.getDeposit());
    assertEquals('8', rs.getContamination());
    assertEquals("99", rs.getDepositDepth());
    assertEquals("99", rs.getBrakingAction());

    rs = r.getRunwayStatesInfo().getRunwayStates().get(1);
    assertEquals("06", rs.getDesignator());
    assertEquals('2', rs.getDeposit());
    assertEquals('0', rs.getContamination());
    assertEquals("39", rs.getDepositDepth());
    assertEquals("//", rs.getBrakingAction());
  }

  @Test
  public void testParseNosig() {
    Parser p = new EUParser();
    Report r = p.parse(LKPD);

    assertNotNull(r.getTrendInfo());
    assertTrue(r.getTrendInfo().isIsNoSignificantChange());
    assertNull(r.getTrendInfo().getTrends());
  }

  @Test
  public void testParseOther() {
    String s = "METAR LKPR 131400Z VRB02KT CAVOK 08/M03 Q1024 NOSIG";
    Parser p = new EUParser();
    Report r;

    r = p.parse(s);
  }

  @Test
  public void testParseOtherA() {
    String s = "METAR LBGO 151000Z AUTO 29003KT 240V330 9999 SCT062/// OVC080/// 08/06 Q1024 R99/19//95 NOSIG";
    Parser p = new EUParser();
    Report r;
    r = p.parse(s);
  }

  @Test
  public void testParseTrend() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertNotNull(r.getTrendInfo());
    assertFalse(r.getTrendInfo().isIsNoSignificantChange());
    assertEquals(1, r.getTrendInfo().getTrends().size());

    TrendReport tr = r.getTrendInfo().getTrends().get(0);
    assertEquals(TrendReportType.BECMG, tr.getType());
  }

  @Test
  public void testParseTrendVisibilityCavok() {
    Parser p = new EUParser();
    Report r = p.parse(LKMT);

    assertNotNull(r.getTrendInfo());
    assertFalse(r.getTrendInfo().isIsNoSignificantChange());
    assertEquals(1, r.getTrendInfo().getTrends().size());
    TrendReport tr = r.getTrendInfo().getTrends().get(0);

    assertTrue(tr.getVisibility().isCAVOK());
    try {
      tr.getVisibility().getVisibilityInMeters();
      fail();
    } catch (Throwable t) {
    }
  }

  @Test
  public void testParseTrendVisibility() {
    Parser p = new EUParser();
    Report r = p.parse(LKPR);

    assertNotNull(r.getTrendInfo());
    assertFalse(r.getTrendInfo().isIsNoSignificantChange());
    assertEquals(1, r.getTrendInfo().getTrends().size());
    TrendReport tr = r.getTrendInfo().getTrends().get(0);

    assertFalse(tr.getVisibility().isCAVOK());
    assertNotNull(tr.getVisibility().getVisibilityInMeters());
    assertEquals(4000, (int) tr.getVisibility().getVisibilityInMeters());
  }

  @Test
  public void testParseOtherC() {
    String s = "METAR LDSP 151000Z 09011KT CAVOK 16/M04 Q1026 BECMG 12008KT";
    Parser p = new EUParser();
    Report r;
    r = p.parse(s);
  }
}
