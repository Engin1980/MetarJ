package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.Report;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.ReportType;
import eng.metarJava.support.DayHourMinute;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class CanadaFormatterTest {
  
    private Report generateReport() {
    Report ret = new Report();
    ret.setType(ReportType.METAR);
    ret.setDayTime(new DayHourMinute(7, 15, 50));
    ret.setIcao("CYVR");
    ret.setWind(WindInfo.createCalm());
    ret.setVisibility(VisibilityInfo.createCAVOK());
    ret.setClouds(CloudInfo.createAsNoDetected());

    return ret;
  }
  
  @Test
  public void testFormatCLR() {
    Report r = generateReport();
    CloudInfo ci = CloudInfo.createAsNoDetected();
    r.setClouds(ci);
    Formatter f = new CanadaFormatter();
    
    String exp = "METAR CYVR 071550Z 00000KT CLR 00/00 A2992";
    String act = f.format(r);
    
    assertEquals(exp, act);
  }
  
  @Test
  public void testFormatSKC() {
    Report r = generateReport();
    CloudInfo ci = CloudInfo.createAsNoSignificant();
    r.setClouds(ci);
    Formatter f = new CanadaFormatter();

    String exp = "METAR CYVR 071550Z 00000KT SKC 00/00 A2992";
    String act = f.format(r);
    
    assertEquals(exp, act);
  }
  
}
