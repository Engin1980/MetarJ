package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayStatesInfo;
import eng.metarJava.RunwayWindshearInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.enums.PhenomenaType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exceptions.NullArgumentException;
import eng.metarJava.support.ReadOnlyList;

/**
 *
 * @author Marek Vajgl
 */
public class GenericFormatterHelper {

  /**
   * Formats wind with unit.
   *
   * @param windInfo wind info, cannot be null
   * @param unit wind speed unit to be used in format. Conversion is to correct speed is done automatically.
   * @param appendSpace true to append a space after a formatted text
   * @return string representing part of report
   */
  public static String formatWind(WindInfo windInfo, SpeedUnit unit, boolean appendSpace) {

    if (windInfo == null) {
      throw new NullArgumentException("wi");
    }

    StringBuilder sb = new StringBuilder();

    if (windInfo.isVariable()) {
      sb.append("VRB");
    } else {
      sb.append(String.format("%03d", windInfo.getDirection().getValue()));
    }
    sb.append(String.format("%02d", windInfo.getSpeed().getIntValue(unit)));
    if (windInfo.isGusting()) {
      sb.append(String.format("G%02d", windInfo.getGustingSpeed().getIntValue(unit)));
    }
    sb.append(unit.toString());
    if (windInfo.isVariating()) {
      sb.append(String.format(" %03dV%03d",
              windInfo.getVariation().getFrom().getValue(),
              windInfo.getVariation().getTo().getValue()));
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * Generate phenomenas string. If null, nothing is generated.
   *
   * @param phenomenas List of phenomenas (may be empty), or null
   * @param appendSpace
   * @return
   */
  public static String formatPhenomenas(ReadOnlyList<PhenomenaInfo> phenomenas, boolean appendSpace) {
    if (phenomenas == null || phenomenas.isEmpty()) {
      return "";
    }
    boolean isFirst = true;

    StringBuilder sb = new StringBuilder();
    for (PhenomenaInfo p : phenomenas) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(" ");
      }

      switch (p.getIntensity()) {
        case heavy:
          sb.append("+");
          break;
        case light:
          sb.append("-");
          break;
      }
      if (p.isInVicinity()) {
        sb.append("VC");
      }
      for (PhenomenaType type : p.getTypes()) {
        sb.append(type.toString());
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  public static String formatClouds(CloudInfo ci, boolean appendSpace) {
    boolean isFirst = true;

    if (ci == null) {
      throw new NullArgumentException("ci");
    }

    StringBuilder sb = new StringBuilder();
    if (ci.isNCD()) {
      sb.append("NCD");
    } else if (ci.isNSC()) {
      sb.append("NSC");
    } else if (ci.isVerticalVisibility()) {
      if (ci.getVerticalVisibilityInHundredFeet() == null) {
        sb.append("VV///");
      } else {
        sb.append("VV").append(String.format("%03d", ci.getVerticalVisibilityInHundredFeet()));
      }
    } else {
      for (CloudMass cm : ci.getMasses()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }
        if (cm.isAmountAndBaseHeightKnown()) {
          sb
                  .append(cm.getAmount().toString())
                  .append(String.format("%03d", cm.getBaseHeightHundredFeet()));

        } else {
          sb.append("//////");
        }
        switch (cm.getSignificantFlag()) {
          case CB:
            sb.append("CB");
            break;
          case TCU:
            sb.append("TCU");
            break;
          case undetected:
            sb.append("///");
            break;
        }
      }
    }

    if (appendSpace && sb.length() > 0) {
      sb.append(" ");
    }
    return sb.toString();
  }

  public static String formatTemperatureDewPoint(int temperature, int dewPoint, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    if (temperature < 0) {
      sb.append("M");
    }
    sb.append(String.format("%02d", Math.abs(temperature)));
    sb.append("/");
    if (dewPoint < 0) {
      sb.append("M");
    }
    sb.append(String.format("%02d", Math.abs(dewPoint)));

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  /**
   * Generate recent phenomena string. If null or empty, nothing is generated.
   *
   * @param recentPhenomenas
   * @param appendSpace
   * @return
   */
  public static String formatRecentPhenomenas(ReadOnlyList<PhenomenaInfo> recentPhenomenas, boolean appendSpace) {
    if (recentPhenomenas == null || recentPhenomenas.isEmpty()) {
      return "";
    }
    boolean isFirst = true;

    StringBuilder sb = new StringBuilder();
    for (PhenomenaInfo p : recentPhenomenas) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(" ");
      }

      sb.append("RE");

      // recent phenomena does not have intensity
      if (p.isInVicinity()) {
        sb.append("VC");
      }
      for (PhenomenaType type : p.getTypes()) {
        sb.append(type.toString());
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * Generates wind-shear info. If null or empty, nothing is generated.
   *
   * @param rwi
   * @param appendSpace
   * @return
   */
  public static String formatWindShears(RunwayWindshearInfo rwi, boolean appendSpace) {
    if (rwi == null || rwi.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    if (rwi.isAllRunways()) {
      sb.append("WS ALL RWY");
    } else {
      for (String rd : rwi.getRunwayDesignators()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }
        sb.append("WS R").append(rd);
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  public static String formatRunwayStatesInfo(RunwayStatesInfo rsi, boolean appendSpace) {
    if (rsi == null || rsi.isEmpty()) {
      return "";
    }

    boolean isFirst = true;
    StringBuilder sb = new StringBuilder();
    if (rsi.isSnowClosed()) {
      sb.append("SNOCLO");
    } else {
      for (RunwayState rs : rsi.getRunwayStates()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }

        sb.append("R").append(rs.getDesignator()).append("/");
        sb.append(rs.getDeposit());
        sb.append(rs.getContamination());
        sb.append(rs.getDepositDepth());
        sb.append(rs.getBrakingAction());
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  public static String formatRemark(String remark) {
    if (remark != null) {
      return "RMK " + remark;
    } else {
      return "";
    }
  }
}
