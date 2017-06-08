package eng.metarJava.decoders;

import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import eng.metarJava.WindInfo;
import static eng.metarJava.decoders.EAssert.assertStringStarts;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.PhenomenaIntensity;
import eng.metarJava.support.PhenomenaType;
import eng.metarJava.support.Speed;
import eng.metarJava.support.Variation;
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
}
