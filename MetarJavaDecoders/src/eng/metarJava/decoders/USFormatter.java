package eng.metarJava.decoders;

import eng.metarJava.Report;
import eng.metarJava.decoders.exceptions.FormatException;
import eng.metarJava.decoders.fields.ReportField;
import eng.metarJava.decoders.support.GenericFormatterHelper;
import eng.metarJava.decoders.support.USFormatterHelper;
import eng.metarJava.enums.PressureUnit;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class USFormatter implements Formatter {

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
      sb.append(USFormatterHelper.formatWind(report.getWind(), SpeedUnit.KT, true));
      sb.append(USFormatterHelper.formatVisibility(report.getVisibility(), true));
      sb.append(USFormatterHelper.formatRunwayVisibility(report.getRunwayVisualRanges(), true));
      sb.append(GenericFormatterHelper.formatPhenomenas(report.getPhenomenas(), true));
      sb.append(USFormatterHelper.formatClouds(report.getClouds(), true));
      sb.append(GenericFormatterHelper.formatTemperatureDewPoint(report.getTemperature(), report.getDewPoint(), true));
      sb.append(USFormatterHelper.formatPressure(report.getPressure(PressureUnit.inHq), true));
      sb.append(GenericFormatterHelper.formatRecentPhenomenas(report.getRecentPhenomenas(), true));
      sb.append(GenericFormatterHelper.formatWindShears(report.getRunwayWindshears(), true));
      sb.append(GenericFormatterHelper.formatRunwayStatesInfo(report.getRunwayStatesInfo(), true));
      // trends skipped as in US no trends are reported
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
}
