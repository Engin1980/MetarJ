package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public class Shared {
  
  /**
   * Checks if value is between min (inclusive) and max (exclusive).
   * @param value
   * @param min
   * @param max
   * @return 
   */
  public static boolean isBetween(int value, int min, int max){
    if (value < min)
      return false;
    else if (value >= max)
      return false;
    return true;
  }

/**
 * Checks if value is between min (inclusive) and max (exclusive).
 * @param value
 * @param min
 * @param max
 * @return 
 */  
  public static boolean isBetween(double value, double min, double max){
    if (value < min)
      return false;
    else if (value >= max)
      return false;
    return true;
  }
}
