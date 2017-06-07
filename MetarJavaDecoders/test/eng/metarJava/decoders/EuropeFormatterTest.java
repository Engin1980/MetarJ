package eng.metarJava.decoders;

import eng.metarJava.Report;
import eng.metarJava.WindInfo;
import static eng.metarJava.decoders.EAssert.assertStringStarts;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
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
  
  private Report generateLKMT(){
    Report ret = new Report();
    ret.setType(ReportType.METAR);
    ret.setDayTime(new DayHourMinute(7, 15, 50));
    ret.setIcao("LKMT");
    ret.setWind(WindInfo.create(new Heading(240), new Speed(13, SpeedUnit.KT)));
    
    return ret;
  }
  
  private Report generateLKPR(){
    Report ret = new Report();
    ret.setType(ReportType.METAR);
    ret.setDayTime(new DayHourMinute(7, 15, 50));
    ret.setIcao("LKPR");
    ret.setWind(WindInfo.create(
            new Heading(240), 
            new Speed(13, SpeedUnit.KT), 
            new Speed(25, SpeedUnit.KT), 
            new Variation<>(new Heading(210), new Heading(250))));
    
    return ret;
  }
  
  private Report generateLKTB(){
    Report ret = new Report();
    ret.setType(ReportType.METAR);
    ret.setDayTime(new DayHourMinute(7, 15, 50));
    ret.setIcao("LKTB");
    ret.setWind(WindInfo.createCalm());
    
    return ret;
  }

  @Test
  public void testFormatType() {
    Report ret = generateLKMT();
    String s = new EuropeFormatter().format(ret);
    assertStringStarts("METAR ", s);
  }
  
  @Test
  public void testFormatICAO() {
    Report ret = generateLKMT();
    assertEquals("LKMT", ret.getIcao());
  }
  
  @Test
  public void testWind(){
    Report ret = generateLKMT();
    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKMT 071550Z 24013KT";
    
    assertStringStarts(exp, act);
  }
  
  @Test
  public void testWindGusting(){
    Report ret = generateLKPR();
    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKPR 071550Z 24013G25KT 210V250";
    
    assertStringStarts(exp, act);
  }
  
  @Test
  public void testWindCalm(){
    Report ret = generateLKTB();
    String act = new EuropeFormatter().format(ret);
    String exp = "METAR LKTB 071550Z 00000KT";
    
    assertStringStarts(exp, act);
  }
  
}
