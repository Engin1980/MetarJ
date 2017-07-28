package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.Report;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marek Vajgl
 */
public class USParserTest {

  private final static String A = "KATL 281152Z 22006KT 8SM 23/23 A2994 RMK AO2 SLP128 70010 T02330228 10239 20228 53003";
  private final static String B = "KATL 281152Z 22006KT 8SM CLR 23/23 A2994 RMK AO2 SLP128 70010 T02330228 10239 20228 53003";

  @Test
  public void testParseSKC() {
    String s = A;
    Parser parser = new USParser();

    Report r = parser.parse(s);

    CloudInfo ci = r.getClouds();
    assertTrue(ci.isNoSignificant());
  }

  @Test
  public void testParseCLR() {
    String s = B;
    Parser parser = new USParser();

    Report r = parser.parse(s);

    CloudInfo ci = r.getClouds();
    assertTrue(ci.isNoDetected());
  }

}
