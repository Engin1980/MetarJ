package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.Report;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class CanadaParserTest {
  
  private final static String A = "METAR CYVR 271517Z 11011G17KT 30SM SKC 15/12 A3010 RMK SC7CU1AC1 CU TR SLP194";
  private final static String B = "METAR CYVR 271517Z 11011G17KT 30SM CLR 15/12 A3010 RMK SC7CU1AC1 CU TR SLP194";
  
  public CanadaParserTest() {
  }

  @Test
  public void testParseSKC() {
    String s = A;
    CanadaParser parser = new CanadaParser();
    
    Report r = parser.parse(s);
    
    CloudInfo ci = r.getClouds();
    assertTrue(ci.isNoSignificant());
  }
  
  @Test
  public void testParseCLR() {
    String s = B;
    CanadaParser parser = new CanadaParser();
    
    Report r = parser.parse(s);
    
    CloudInfo ci = r.getClouds();
    assertTrue(ci.isNoDetected());
  }
  
}
