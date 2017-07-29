package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exceptions.NullArgumentException;
import eng.metarJava.support.ReadOnlyList;
import eng.metarJava.support.Variation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marek Vajgl
 */
public class USFormatterHelper {

  private static Map<Double, String> smFractionValues = new HashMap<>();

  static {
    smFractionValues.put(1 / 16d, "1/16");
    smFractionValues.put(2 / 16d, "1/8");
    smFractionValues.put(3 / 16d, "3/16");
    smFractionValues.put(4 / 16d, "1/4");
    smFractionValues.put(5 / 16d, "5/16");
    smFractionValues.put(6 / 16d, "3/8");
    smFractionValues.put(4 / 8d, "1/2");
    smFractionValues.put(5 / 8d, "5/8");
    smFractionValues.put(6 / 8d, "3/4");
    smFractionValues.put(7 / 8d, "7/8");
    smFractionValues.put(8 / 8d, "1");
    smFractionValues.put(9 / 8d, "1 1/8");
    smFractionValues.put(10 / 8d, "1 1/4");
    smFractionValues.put(11 / 8d, "1 3/8");
    smFractionValues.put(12 / 8d, "1 1/2");
    smFractionValues.put(13 / 8d, "1 5/8");
    smFractionValues.put(14 / 8d, "1 3/4");
    smFractionValues.put(15 / 8d, "1 7/8");
    smFractionValues.put(16 / 8d, "2");
    smFractionValues.put(9 / 4d, "2 1/4");
    smFractionValues.put(10 / 4d, "2 1/2");
    smFractionValues.put(11 / 4d, "2 3/4");
    smFractionValues.put(12 / 4d, "3");
  }

  public static String formatWind(WindInfo wi, SpeedUnit unit, boolean appendSpace) {

    StringBuilder sb = new StringBuilder();
    if (wi != null) {
      String tmp = GenericFormatterHelper.formatWind(wi, unit, true);
      sb.append(tmp);
    }

    return sb.toString();
  }

  /**
   * Formats visibility. If CAVOK, returns empty string.
   *
   * @param vi
   * @param appendSpace
   * @return
   */
  public static String formatVisibility(VisibilityInfo vi, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    if (vi.isCAVOK()) {
      return ""; // CAVOK means no significant visibility, and in US, in this case nothing is reported.
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

    if (value > 3) {
      sb.append((int) Math.floor(value + 0.2));
    } else {
      String minFrac = "";
      double minDiff = Double.MAX_VALUE;
      for (Double key : smFractionValues.keySet()) {
        double diff = Math.abs(value - key);
        if (diff < minDiff){
          minDiff = diff;
          minFrac = smFractionValues.get(key);
        }
      }
      sb.append(minFrac);
    }
    
    sb.append("SM");

    return sb.toString();
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
      sb.append("FT");
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
    if (ci.isNoSignificant()) {
      sb.append("");
    } else if (ci.isNoDetected()) {
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
    int tmp = (int) Math.round(pressureInInHg * 100);
    sb.append("A").append(String.format("%04d", tmp));
    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }
}
