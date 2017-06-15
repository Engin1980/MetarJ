package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.Report;
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

    double intPart = (int) Math.floor(value);
    double restPart = value - intPart;

    if (intPart > 0 && (restPart == 0 || intPart > 3)) {
      sb.append(intPart + "SM");
    } else if (intPart > 0) {
      sb.append(intPart + " ");
    }

    if (restPart != 0) {
      if (value <= 3 / 8d) {
        int nrst = findNearestFraction(restPart, 16);
        sb.append(nrst).append("/").append("16SM");
      } else if (value <= 2) {
        int nrst = findNearestFraction(restPart, 8);
        sb.append(nrst).append("/").append("8SM");
      } else { // if (value <= 3)
        int nrst = findNearestFraction(restPart, 4);
        sb.append(nrst).append("/").append("4SM");
      }
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
        sb.append(String.format("%04.0fV%04.0f",
                varInFt.getFrom(),
                varInFt.getTo()));
      } else {
        double visInFt = rvr.getVisibility(DistanceUnit.feet);
        sb.append(String.format("%04.0f", visInFt));
      }

    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * NCD is SKC in US
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
      sb.append("SKC");
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
    sb.append("A").append(String.format("%04.2f", pressureInInHg));

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }
}
