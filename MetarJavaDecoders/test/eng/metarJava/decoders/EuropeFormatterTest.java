package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayStatesInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.RunwayWindshearInfo;
import eng.metarJava.TrendInfo;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import eng.metarJava.WindInfo;
import static eng.metarJava.decoders.EAssert.assertStringStarts;
import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.PhenomenaIntensity;
import eng.metarJava.support.PhenomenaType;
import eng.metarJava.support.Speed;
import eng.metarJava.support.Variation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class EuropeFormatterTest {

  public EuropeFormatterTest() {
  }

  private Report generateReport() {
    Report ret = new Report();
    ret.setType(ReportType.METAR);
    ret.setDayTime(new DayHourMinute(7, 15, 50));
    ret.setIcao("LKMT");
    ret.setWind(WindInfo.createCalm());
    ret.setVisibility(VisibilityInfo.createCAVOK());
    ret.setClouds(CloudInfo.createNCD());

    return ret;
  }

  @Test
  public void testFormatType() {
    Report ret = generateReport();
    String s = new EuropeFormatter().format(ret);
    assertStringStarts("METAR ", s);
  }

  @Test
  public void testFormatCOR() {
    Report ret = generateReport();
    ret.setCorrection(true);

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR COR ";
    assertStringStarts(exp, act);
  }

  @Test
  public void testFormatNIL() {
    Report ret = generateReport();
    ret.setNil(true);

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z NIL";
    assertStringStarts(exp, act);
  }

  @Test
  public void testFormatICAO() {
    Report ret = generateReport();
    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT";
    assertStringStarts(exp, act);
  }

  @Test
  public void testWind() {
    Report ret = generateReport();
    ret.setWind(WindInfo.create(
            new Heading(240),
            new Speed(13, SpeedUnit.KT)));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 24013KT";
    assertStringStarts(exp, act);
  }

  @Test
  public void testWindGusting() {
    Report ret = generateReport();
    ret.setWind(WindInfo.create(
            new Heading(240),
            new Speed(13, SpeedUnit.KT),
            new Speed(25, SpeedUnit.KT),
            new Variation<Heading>(new Heading(210), new Heading(250))));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 24013G25KT 210V250";

    assertStringStarts(exp, act);
  }

  @Test
  public void testWindCalm() {
    Report ret = generateReport();
    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT";

    assertStringStarts(exp, act);
  }

  @Test
  public void testVisibilityCAVOK() {
    Report ret = generateReport();
    ret.setVisibility(VisibilityInfo.createCAVOK());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK";

    assertStringStarts(exp, act);
  }

  @Test
  public void testVisibilityNumber() {
    Report ret = generateReport();
    ret.setVisibility(VisibilityInfo.create(5000));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT 5000";

    assertStringStarts(exp, act);
  }

  @Test
  public void testVisibilityNumberNDV() {
    Report ret = generateReport();
    ret.setVisibility(VisibilityInfo.createWithNDV(5000));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT 5000NDV";

    assertStringStarts(exp, act);
  }

  @Test
  public void testVisibilityVaring() {
    Report ret = generateReport();
    ret.setVisibility(
            VisibilityInfo.create(
                    5000,
                    VisibilityVariability.create(2000, Direction.north)));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT 5000 2000N";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayVisibilityNumber() {
    Report ret = generateReport();
    ret.getRunwayVisualRanges().clear();
    ret.getRunwayVisualRanges().add(
            RunwayVisualRange.create("24C", 300));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK R24C/0300";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayVisibilityVariating() {
    Report ret = generateReport();
    ret.getRunwayVisualRanges().clear();
    ret.getRunwayVisualRanges().add(
            RunwayVisualRange.create("24C", new Variation<>(50, 200)));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK R24C/0050V0200";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayVisibilityVariatingMultiple() {
    Report ret = generateReport();
    ret.getRunwayVisualRanges().clear();
    ret.getRunwayVisualRanges().add(
            RunwayVisualRange.create("24C", new Variation<>(50, 200)));
    ret.getRunwayVisualRanges().add(
            RunwayVisualRange.create("06C", 150));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK R24C/0050V0200 R06C/0150";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayPhenomenaA() {
    Report ret = generateReport();
    ret.getPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.heavy, PhenomenaType.RA, false));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK +RA";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayPhenomenaB() {
    Report ret = generateReport();
    ret.getPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.moderate, new PhenomenaType[]{PhenomenaType.TS, PhenomenaType.RA}, true));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK VCTSRA";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRunwayPhenomenaC() {
    Report ret = generateReport();
    ret.getPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.light, new PhenomenaType[]{PhenomenaType.RA}, false));
    ret.getPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.moderate, new PhenomenaType[]{PhenomenaType.BL, PhenomenaType.SN}, true));
    ret.getPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.heavy, new PhenomenaType[]{PhenomenaType.SN, PhenomenaType.SH}, false));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK -RA VCBLSN +SNSH";

    assertStringStarts(exp, act);
  }

  @Test
  public void testCloudNSC() {
    Report ret = generateReport();
    ret.setClouds(CloudInfo.createNSC());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NSC";

    assertStringStarts(exp, act);
  }

  @Test
  public void testCloudNCD() {
    Report ret = generateReport();
    ret.setClouds(CloudInfo.createNCD());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD";

    assertStringStarts(exp, act);
  }

  @Test
  public void testCloudVV() {
    Report ret = generateReport();
    ret.setClouds(CloudInfo.createWithVV((Integer) 300));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK VV300";

    assertStringStarts(exp, act);
  }

  @Test
  public void testCloudVVUnknown() {
    Report ret = generateReport();
    ret.setClouds(CloudInfo.createWithUnknownVV());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK VV///";

    assertStringStarts(exp, act);
  }

  @Test
  public void testCloudWithMasses() {
    Report ret = generateReport();
    List<CloudMass> cms = new ArrayList();
    cms.add(CloudMass.create(CloudAmount.SCT, 40));
    cms.add(CloudMass.create(CloudAmount.OVC, 70, CloudMassSignificantFlag.TCU));
    cms.add(CloudMass.create(CloudAmount.FEW, 90, CloudMassSignificantFlag.CB));
    cms.add(CloudMass.create(CloudAmount.OVC, 100, CloudMassSignificantFlag.undetected));
    cms.add(CloudMass.create(CloudAmount.BKN, 120));
    cms.add(CloudMass.createWithoutAmountAndBaseHeightCB());
    cms.add(CloudMass.createWithoutAmountAndBaseHeightTCU());
    ret.setClouds(CloudInfo.create(cms));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK SCT040 OVC070TCU FEW090CB OVC100/// BKN120 //////CB //////TCU";

    assertStringStarts(exp, act);
  }

  @Test
  public void testTemperatureA() {
    Report ret = generateReport();
    ret.setTemperature(10);
    ret.setDewPoint(5);

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 10/05";

    assertStringStarts(exp, act);
  }

  @Test
  public void testTemperatureB() {
    Report ret = generateReport();
    ret.setTemperature(-10);
    ret.setDewPoint(-5);

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD M10/M05";

    assertStringStarts(exp, act);
  }

  @Test
  public void testPressure() {
    Report ret = generateReport();
    ret.setPressureInHpa(995);

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0995";

    assertStringStarts(exp, act);
  }

  @Test
  public void testRecentPhenomena() {
    Report ret = generateReport();
    ret.getRecentPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.moderate, PhenomenaType.RA, false));
    ret.getRecentPhenomenas().add(
            PhenomenaInfo.create(PhenomenaIntensity.moderate, new PhenomenaType[]{PhenomenaType.SN, PhenomenaType.SH}, false));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 RERA RESNSH";

    assertStringStarts(exp, act);
  }
  
  @Test
  public void testWindShears() {
    Report ret = generateReport();
    ret.setRunwayWindshears(RunwayWindshearInfo.createAllRWY());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 WS ALL RWY";

    assertStringStarts(exp, act);
  }
  
    @Test
  public void testWindShearsSpecific() {
    Report ret = generateReport();
    Set<String>rwys = new TreeSet();
    rwys.add("06");
    rwys.add("24L");
    rwys.add("24R");
    ret.setRunwayWindshears(RunwayWindshearInfo.create(rwys));

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 WS R06 R24L R24R";

    assertStringStarts(exp, act);
  }
    
  @Test
  public void testRunwayStateSNOCLO() {
    Report ret = generateReport();
    ret.setRunwayStatesInfo(
            RunwayStatesInfo.createSNOCLO()
    );

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 SNOCLO";

    assertStringStarts(exp, act);
  }
  
    @Test
  public void testRunwayState() {
    Report ret = generateReport();
    List<RunwayState> rs = new ArrayList();
    rs.add(RunwayState.create("24", '3', '1', "23", "52"));
    rs.add(RunwayState.create("06", '/', '/', "//", "99"));
    ret.setRunwayStatesInfo(
            RunwayStatesInfo.create(rs)
    );

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 R24/312352 R06/////99";

    assertStringStarts(exp, act);
  }
  
      @Test
  public void testNOSIG() {
    Report ret = generateReport();
    ret.setTrendInfo(TrendInfo.createNOSIG());

    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 00000KT CAVOK NCD 00/00 Q0000 NOSIG";

    assertStringStarts(exp, act);
  }
}
