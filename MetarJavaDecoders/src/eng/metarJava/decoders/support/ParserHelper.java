package eng.metarJava.decoders.support;

/**
 *
 * @author Marek Vajgl
 */
public abstract class ParserHelper {
  
  public static int groupToInt(String txt) {
    int ret = Integer.parseInt(txt);
    return ret;
  }
  
  public static boolean groupExist(String groupText) {
    return groupText != null;
  }
}
