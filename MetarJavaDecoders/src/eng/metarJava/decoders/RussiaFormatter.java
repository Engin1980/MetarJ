package eng.metarJava.decoders;

import eng.metarJava.CloudMass;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.TrendCloudInfo;
import eng.metarJava.TrendPhenomenaInfo;
import eng.metarJava.TrendReport;
import eng.metarJava.TrendReportTimeInfo;
import eng.metarJava.TrendVisibilityInfo;
import eng.metarJava.WindInfo;
import eng.metarJava.decoders.exceptions.FormatException;
import eng.metarJava.decoders.support.EUFormatterHelper;
import eng.metarJava.decoders.support.GenericFormatterHelper;
import eng.metarJava.decoders.support.ReportField;
import eng.metarJava.enums.PhenomenaType;
import eng.metarJava.enums.PressureUnit;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class RussiaFormatter implements Formatter {

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
      sb.append(EUFormatterHelper.formatWind(report.getWind(), SpeedUnit.MPS, true));
      sb.append(EUFormatterHelper.formatVisibility(report.getVisibility(), true));
      sb.append(EUFormatterHelper.formatRunwayVisibility(report.getRunwayVisualRanges(), true));
      sb.append(GenericFormatterHelper.formatPhenomenas(report.getPhenomenas(), true));
      sb.append(GenericFormatterHelper.formatClouds(report.getClouds(), true));
      sb.append(GenericFormatterHelper.formatTemperatureDewPoint(report.getTemperature(), report.getDewPoint(), true));
      sb.append(EUFormatterHelper.formatPressure(report.getPressure(PressureUnit.hpa), true));
      sb.append(GenericFormatterHelper.formatRecentPhenomenas(report.getRecentPhenomenas(), true));
      sb.append(GenericFormatterHelper.formatWindShears(report.getRunwayWindshears(), true));
      sb.append(GenericFormatterHelper.formatRunwayStatesInfo(report.getRunwayStatesInfo(), true));
      sb.append(formatTrendInfo(report, true));
      sb.append(GenericFormatterHelper.formatRemark(report.getRemark()));
    }

    while (sb.charAt(sb.length() - 1) == ' ') {
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

  private String formatTrendInfo(Report report, boolean appendSpace) {
    if (report.getTrendInfo() == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    if (report.getTrendInfo().isIsNoSignificantChange()) {
      sb.append("NOSIG");
    } else {
      boolean isFirst = true;
      for (TrendReport trend : report.getTrendInfo().getTrends()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }
        sb.append(formatTrend(trend, true));
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }

    return sb.toString();
  }

  private String formatTrend(TrendReport trendReport, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    sb.append(formatTrendType(trendReport, true));
    sb.append(formatTrendTime(trendReport, true));
    sb.append(formatTrendWind(trendReport, true));
    sb.append(formatTrendVisibility(trendReport, true));
    sb.append(formatTrendPhenomena(trendReport, true));
    sb.append(formatTrendClouds(trendReport, true));

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatTrendType(TrendReport tr, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    sb.append(tr.getType().toString());
    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatTrendTime(TrendReport tr, boolean appendSpace) {
    TrendReportTimeInfo timeInfo = tr.getTime();
    if (timeInfo == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append(timeInfo.getIndication().toString());
    sb.append(String.format("%02d%02d", timeInfo.getTime().getHour(), timeInfo.getTime().getMinute()));

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();

  }

  private String formatTrendWind(TrendReport tr, boolean appendSpace) {
    if (tr.getWind() == null) {
      return "";
    }

    WindInfo wi = tr.getWind();
    StringBuilder sb = new StringBuilder();

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

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatTrendVisibility(TrendReport tr, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();

    TrendVisibilityInfo vi = tr.getVisibility();
    if (vi == null) {
      return "";
    }

    if (vi.isCAVOK()) {
      sb.append("CAVOK");
    } else {
      sb.append(String.format("%04d", (int) vi.getVisibilityInMeters()));
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatTrendPhenomena(TrendReport tr, boolean appendSpace) {
    TrendPhenomenaInfo tpi = tr.getPhenomenas();
    if (tpi == null || tpi.isEmpty()) {
      // no trend-phenomena object, or trend-phenomena object with not NSW flag and empty phenomenas.
      return "";
    }

    StringBuilder sb = new StringBuilder();

    if (tpi.isNSW()) {
      sb.append("NSW");
    } else {
      boolean isFirst = true;

      for (PhenomenaInfo p : tpi.getPhenomenas()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(" ");
        }

        // trend phenomenas should not have intensities other than moderate
        if (p.isInVicinity()) {
          sb.append("VC");
        }
        for (PhenomenaType type : p.getTypes()) {
          sb.append(type.toString());
        }
      }
    }

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

  private String formatTrendClouds(TrendReport tr, boolean appendSpace) {
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;

    TrendCloudInfo ci = tr.getClouds();
    if (ci == null || ci.isEmpty()) {
      return "";
    }

    if (ci.isNoSignificant()) {
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

    if (appendSpace) {
      sb.append(" ");
    }
    return sb.toString();
  }

}
