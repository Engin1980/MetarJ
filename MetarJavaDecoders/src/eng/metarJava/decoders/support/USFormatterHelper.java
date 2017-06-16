package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.ReadOnlyList;
import eng.metarJava.support.Variation;

/**
 *
 * @author Marek Vajgl
 */
public class USFormatterHelper {

  public static String formatWind(WindInfo wi, SpeedUnit unit, boolean appendSpace) {

    StringBuilder sb = new StringBuilder();
    if (wi != null) {
      String tmp = GenericFormatterHelper.formatWind(wi, unit, true);
      sb.append(tmp);
    }

    return sb.toString();
  }

  /**
   * Formats visibility. If CAVOK, fixed value as 15SM.
   *
   * @param vi
   * @param appendSpace
   * @return
   */
  public static String formatVisibility(VisibilityInfo vi, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    if (vi.isCAVOK()) {
      sb.append("15SM");
    } else {
      double vis = (vi.getVisibility(DistanceUnit.miles));
      String visAsString = convertDoubleValueToRational(vis);

      sb.append(visAsString);
      if (vi.isVariating()) {
        sb
                .append(" ")
                .append(String.format("%04d", vi.getVariability().getVisibilityInMeters()))
                .append(vi.getVariability().getDirection().toString());
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private static String convertDoubleValueToRational(double value) {
    StringBuilder sb = new StringBuilder();

//    double integer = 0;
//    double numerator = 0;
//    double denominator = 0;
//    integer = (int) Math.floor(value);
//    value = value - integer;
//
//    {
//      denominator = 16;
//      double maxDiff = Double.MAX_VALUE;
//      for (int i = 0; i < 16; i++) {
//        double fraction = i / 16d;
//        double diff = Math.abs(value - fraction);
//        if (diff < maxDiff) {
//          maxDiff = diff;
//          numerator = i;
//        }
//      }
//    }
//    
//    // vykraceni
//    if (denominator / numerator == Math.floor(denominator / numerator)){
//      denominator = denominator / numerator;
//      numerator = 1;
//    }
    int integer = (int) Math.floor(value);
    double decimal = value - integer;
    int numerator = 0;
    int denominator = 0;

    if (decimal != 0) {
      if (value <= 3 / 8d) {
        numerator = findNearestFraction(decimal, 16);
        denominator = 16;
      } else if (value <= 2) {
        numerator = findNearestFraction(decimal, 8);
        denominator = 8;
      } else { // if (value <= 3)
        numerator = findNearestFraction(decimal, 4);
        denominator = 4;
      }
    }

    if (numerator != 0) {
      if (denominator / numerator == Math.floor(denominator / numerator)) {
        denominator = denominator / numerator;
        numerator = 1;
      }
      if (numerator == denominator) {
        integer = integer + 1;
        numerator = 0;
      }
    }

    if (numerator == 0 || integer >= 3) {
      sb.append(integer).append("SM");
    } else {
      if (integer > 0) {
        sb.append(integer).append(" ");
      }
      sb.append(numerator).append("/").append(denominator).append("SM");
    }

    return sb.toString();
  }

  private static int findNearestFraction(double number, int fractionSize) {
    double fraction = 1 / (double) fractionSize;
    double maxDiff = fraction / 2;
    for (int i = 0; i < 10; i++) {
      double curr = i * fraction;
      double diff = Math.abs(number - curr);
      if (diff < maxDiff) {
        return i;
      }
    }
    throw new UnsupportedOperationException("You are not supposed to be here.");
  }

  public static String formatRunwayVisibility(ReadOnlyList<RunwayVisualRange> runwayVisualRanges, boolean appendSpace) {
    if (runwayVisualRanges == null || runwayVisualRanges.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;

    for (RunwayVisualRange rvr : runwayVisualRanges) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(" ");
      }

      sb.append("R")
              .append(rvr.getRunwayDesignator())
              .append("/");
      if (rvr.isVariating()) {
        Variation<Double> varInFt = rvr.getVariatingVisibility(DistanceUnit.feet);
        int minVar = (int) Math.round(varInFt.getFrom());
        if (minVar < 1000) {
          sb.append("M");
          minVar = 1000;
        }
        sb.append(String.format("%04d", minVar));
        sb.append("V");
        int maxVar = (int) Math.round(varInFt.getTo());
        if (maxVar > 6000) {
          sb.append("P");
          maxVar = 6000;
        }
        sb.append(String.format("%04d", maxVar));
      } else {
        int vis = (int) Math.round(rvr.getVisibility(DistanceUnit.feet));
        if (vis < 1000) {
          vis = 1000;
          sb.append("M");
        } else if (vis > 6000) {
          vis = 6000;
          sb.append("P");
        }
        sb.append(String.format("%04d", vis));
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * NCD is CLR in US
   *
   * @param report
   * @param appendSpace
   * @return
   */
  public static String formatClouds(CloudInfo ci, boolean appendSpace) {
    if (ci == null) {
      throw new NullArgumentException("ci");
    }

    StringBuilder sb = new StringBuilder();
    if (ci.isNCD()) {
      sb.append("CLR");
    } else {
      String tmp = GenericFormatterHelper.formatClouds(ci, false);
      sb.append(tmp);
    }

    if (appendSpace && sb.length() > 0) {
      sb.append(" ");
    }
    return sb.toString();
  }

  public static String formatPressure(double pressureInInHg, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    int tmp = (int) Math.abs(pressureInInHg * 100);
    sb.append("A").append(String.format("%04d", tmp));
    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }
}
