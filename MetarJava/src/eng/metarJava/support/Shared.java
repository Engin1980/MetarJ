package eng.metarJava.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class Shared {

  /**
   * Checks if value is between min (inclusive) and max (exclusive).
   *
   * @param value
   * @param min
   * @param max
   * @return
   */
  public static boolean isBetween(int value, int min, int max) {
    if (value < min) {
      return false;
    } else if (value >= max) {
      return false;
    }
    return true;
  }

  /**
   * Checks if value is between min (inclusive) and max (exclusive).
   *
   * @param value
   * @param min
   * @param max
   * @return
   */
  public static boolean isBetween(double value, double min, double max) {
    if (value < min) {
      return false;
    } else if (value >= max) {
      return false;
    }
    return true;
  }

  /**
   * Makes a copy of an array into a new array. Returns null if source is null.
   *
   * @param <T> Type of array
   * @param source Source array
   * @return
   */
  public static <T> T[] copyArray(T[] source) {
    T[] ret;
    if (source == null) {
      ret = null;
    } else {
      ret = (T[]) Arrays.copyOf(source, source.length);
    }
    return ret;
  }
}
