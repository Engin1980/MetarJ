package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayStatesInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.RunwayWindshearInfo;
import eng.metarJava.TrendInfo;
import eng.metarJava.TrendReport;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.decoders.exceptions.FormatException;
import eng.metarJava.decoders.fields.ReportField;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.PhenomenaType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class EuropeFormatter implements Formatter {

  @Override
  public String format(Report report) {

    if (checkForErrors(report).isEmpty() == false) {
      throw new FormatException(ReportField.report, FormatException.ErrorType.Other,
              "Unable to format report as it contains unresolved internal errors.");
    }

    StringBuilder sb = new StringBuilder();
    sb.append(report.getType().toString()).append(" ");
    if (report.isCorrection()) {
      sb.append("COR ");
    }
    sb.append(report.getIcao()).append(" ");
    sb.append(
            String.format("%02d%02d%02dZ", report.getDayTime().getDay(), report.getDayTime().getHour(), report.getDayTime().getMinute())
    ).append(" ");
    if (report.isNil()) {
      sb.append("NIL");
    } else {
      // is not NIL

      if (report.isAuto()) {
        sb.append("AUTO ");
      }
      sb.append(formatWind(report, true));
      sb.append(formatVisibility(report, true));
      sb.append(formatRunwayVisibility(report, true));
      sb.append(formatPhenomenas(report, true));
      sb.append(formatClouds(report, true));
      sb.append(formatTemperatureDewPoint(report, true));
      sb.append(formatPressure(report, true));
      sb.append(formatRecentPhenomenas(report, true));
      sb.append(formatWindShears(report, true));
      sb.append(formatRunwayStatesInfo(report, true));
      sb.append(formatTrendInfo(report, true));
      sb.append(formatRemark(report));
    }

    if (sb.charAt(sb.length() - 1) == ' ') {
      sb.deleteCharAt(sb.length() - 1);
    }

    return sb.toString();
  }

  @Override
  public List<Exception> checkForErrors(Report report) {
    List<Exception> errors = new ArrayList<>();

    if (report.getType() == ReportType.UNKNOWN) {
      errors.add(new FormatException(ReportField.reportType,
              FormatException.ErrorType.IsInvalid, "Type must be METAR or SPECI."));
    }

    if (report.getIcao() == null) {
      errors.add(new FormatException(ReportField.icao,
              FormatException.ErrorType.IsNull, "ICAO code cannot be null"));
    }

    if (report.getDayTime() == null) {
      errors.add(new FormatException(ReportField.dayTime,
              FormatException.ErrorType.IsNull, "Day-Time cannot be null."));
    }

    if (report.isNil()) {
      // TODO here should be more tests that everything else is empty.
      return errors;
    }

    if (report.getVisibility() == null) {
      errors.add(new FormatException(ReportField.visibility, FormatException.ErrorType.IsNull, "Visibility cannot be empty."));
    }

    if (report.getClouds() == null) {
      errors.add(new FormatException(ReportField.clouds, FormatException.ErrorType.IsNull, "Cloud cannot be empty."));
    }

    return errors;
  }

  private String formatWind(Report report, boolean appendSpace) {
    WindInfo wi = report.getWind();

    StringBuilder sb = new StringBuilder();

    if (wi == null) {
      sb.append("/////KT");
    } else {
      if (wi.isVariable()) {
        sb.append("VRB");
      } else {
        sb.append(String.format("%03d", wi.getDirection().getValue()));
      }
      sb.append(String.format("%02d", wi.getSpeed().getIntValue(SpeedUnit.KT)));
      if (wi.isGusting()) {
        sb.append(String.format("G%02d", wi.getGustingSpeed().getIntValue(SpeedUnit.KT)));
      }
      sb.append("KT");
      if (wi.isVariating()) {
        sb.append(String.format(" %03dV%03d",
                wi.getVariation().getFrom().getValue(),
                wi.getVariation().getTo().getValue()));
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatVisibility(Report report, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    VisibilityInfo vi = report.getVisibility();

    if (vi.isCAVOK()) {
      sb.append("CAVOK");
    } else {
      sb.append(String.format("%04d", (int) vi.getVisibilityInMeters()));
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

  private String formatRunwayVisibility(Report report, boolean appendSpace) {
    if (report.getRunwayVisualRanges() == null || report.getRunwayVisualRanges().isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;

    for (RunwayVisualRange rvr : report.getRunwayVisualRanges()) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(" ");
      }

      sb.append("R")
              .append(rvr.getRunwayDesignator())
              .append("/");
      if (rvr.isVariating()) {
        sb.append(String.format("%04dV%04d",
                (int) rvr.getVariatingVisibilityInMeters().getFrom(),
                (int) rvr.getVariatingVisibilityInMeters().getTo()));
      } else {
        sb.append(String.format("%04d", rvr.getVisibilityInMeters()));
      }

    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatPhenomenas(Report report, boolean appendSpace) {
    if (report.getPhenomenas() == null || report.getPhenomenas().isEmpty()) {
      return "";
    }
    boolean isFirst = true;

    StringBuilder sb = new StringBuilder();
    for (PhenomenaInfo p : report.getPhenomenas()) {
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

  private String formatClouds(Report report, boolean appendSpace) {
    boolean isFirst = true;

    StringBuilder sb = new StringBuilder();
    CloudInfo ci = report.getClouds();
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

  private String formatTrends(Report report) {
    TrendInfo ti = report.getTrendInfo();
    if (ti.isIsNoSignificantChange()) {
      return "NOSIG";
    } else {
      StringBuilder sb = new StringBuilder();
      boolean isFirst = true;
      for (TrendReport trend : ti.getTrends()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }
        sb.append(formatTrend(trend));
      }
      return sb.toString();
    }
  }

  private String formatTrend(TrendReport trend) {
    //TODO dodelat
    throw new UnsupportedOperationException();
  }

  private String formatTemperatureDewPoint(Report report, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    if (report.getTemperature() < 0) {
      sb.append("M");
    }
    sb.append(String.format("%02d", Math.abs(report.getTemperature())));
    sb.append("/");
    if (report.getDewPoint() < 0) {
      sb.append("M");
    }
    sb.append(String.format("%02d", Math.abs(report.getDewPoint())));

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  private String formatPressure(Report report, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    sb.append("Q").append(String.format("%04d", report.getPressureInHpa()));

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  private String formatRecentPhenomenas(Report report, boolean appendSpace) {
    if (report.getRecentPhenomenas() == null || report.getRecentPhenomenas().isEmpty()) {
      return "";
    }
    boolean isFirst = true;

    StringBuilder sb = new StringBuilder();
    for (PhenomenaInfo p : report.getRecentPhenomenas()) {
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

  private String formatWindShears(Report report, boolean appendSpace) {
    if (report.getRunwayWindshears() == null || report.getRunwayWindshears().isEmpty()) {
      return "";
    }

    RunwayWindshearInfo rwi = report.getRunwayWindshears();
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    if (rwi.isAllRunways()) {
      sb.append("WS ALL RWY");
    } else {
      sb.append("WS ");
      for (String rd : rwi.getRunwayDesignators()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }
        sb.append("R").append(rd);
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  private String formatRunwayStatesInfo(Report report, boolean appendSpace) {
    RunwayStatesInfo rsi = report.getRunwayStatesInfo();
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

  private String formatTrendInfo(Report report, boolean appendSpace) {
    if (report.getTrendInfo() == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    if (report.getTrendInfo().isIsNoSignificantChange()) {
      sb.append("NOSIG");
    } else {
      throw new RuntimeException("Not yet implemented");
    }

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  private String formatRemark(Report report) {
    if (report.getRemark() != null) {
      return "RMK " + report.getRemark();
    } else {
      return "";
    }
  }
}
