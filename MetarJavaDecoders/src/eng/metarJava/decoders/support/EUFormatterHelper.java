package eng.metarJava.decoders.support;

import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.ReadOnlyList;

/**
 *
 * @author Marek Vajgl
 */
public class EUFormatterHelper {
  
  public static String formatWind(WindInfo wi, SpeedUnit unit, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    if (wi == null) {
      sb.append("/////").append(unit.toString());
    } else {
      String tmp = GenericFormatterHelper.formatWind(wi, unit, false);
      sb.append(tmp);
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }
  
  public static String formatVisibility(VisibilityInfo vi, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    if (vi.isCAVOK()) {
      sb.append("CAVOK");
    } else {
      double vis = (vi.getVisibilityInMeters() > 9999) ? 9999 : vi.getVisibilityInMeters();
      sb.append(String.format("%04.0f", vis));
      if (vi.isNoDirectionalVisibility()) {
        sb.append("NDV");
      }
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
        sb.append(String.format("%04.0fV%04.0f",
                rvr.getVariatingVisibilityInMeters().getFrom(),
                rvr.getVariatingVisibilityInMeters().getTo()));
      } else {
        sb.append(String.format("%04.0f", rvr.getVisibilityInMeters()));
      }

    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }
  
  public static String formatPressure(double pressureInHpa, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    sb.append("Q").append(String.format("%04.0f", pressureInHpa));

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }
}
