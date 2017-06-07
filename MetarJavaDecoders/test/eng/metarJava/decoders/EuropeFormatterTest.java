package eng.metarJava.decoders;

import eng.metarJava.Report;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Speed;
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
  
  private void assertStarts(String expected, String actual){
    assertTrue(actual.startsWith(expected));
  }

  @Test
  public void testFormatType() {
    Report ret = generateLKMT();
    
    String s = new EuropeFormatter().format(ret);
    assertStarts("METAR ", s);
  }
  
  @Test
  public void testFormatICAO() {
    Report ret = generateLKMT();
    
    assertEquals("LKMT", ret.getIcao());
  }
  
}
