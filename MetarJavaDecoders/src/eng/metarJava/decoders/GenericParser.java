package eng.metarJava.decoders;

import eng.metarJava.decoders.support.GenericParserHelper;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.TrendInfo;
import eng.metarJava.TrendReport;
import eng.metarJava.decoders.support.TemperatureAndDewPoint;
import eng.metarJava.enums.TrendReportType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
@Deprecated()
public class GenericParser implements Parser {

  @Override
  public Report parse(String line) {

    /**
     * Added a space at the end as most of the regular expressions are expecting "space" as a delimiter before next section.
     */
    ReportLine rl = new ReportLine(line + " ");
    Report ret = new Report();

    ret.setType(GenericParserHelper.decodeReportType(rl));
    ret.setCorrection(GenericParserHelper.decodeCor(rl));
    ret.setIcao(GenericParserHelper.decodeIcao(rl));
    ret.setDayTime(GenericParserHelper.decodeDayTime(rl));
    ret.setNil(GenericParserHelper.decodeNil(rl));
    if (ret.isNil()) {
      return ret;
    }

    ret.setAuto(GenericParserHelper.decodeAuto(rl));
    ret.setWind(GenericParserHelper.decodeWind(rl, true));
    //ret.setVisibility(GenericParserHelper.decodeVisibility(rl));
    //ret.setRunwayVisualRanges(GenericParserHelper.decodeRunwayVisualRanges(rl));
    ret.setPhenomenas(GenericParserHelper.decodePhenomenas(rl));
    //ret.setClouds(GenericParserHelper.decodeClouds(rl));
    TemperatureAndDewPoint tdp = GenericParserHelper.decodeTemperatureAndDewPoint(rl);
    ret.setTemperature(tdp.temperature);
    ret.setDewPoint(tdp.dewPoint);
    //ret.setPressureInHpa(GenericParserHelper.decodePressureInHpa(rl));
    ret.setRecentPhenomenas(GenericParserHelper.decodeRecentPhenomenas(rl));
    ret.setRunwayWindshears(GenericParserHelper.decodeWindShears(rl));
    GenericParserHelper.decodeSeaTemperatureAndState(rl); // not valid for aviation
    ret.setRunwayStatesInfo(GenericParserHelper.decodeRunwayStateInfo(rl));
    ret.setTrendInfo(decodeTrendInfo(rl, false));
    ret.setRemark(GenericParserHelper.decodeRemark(rl));

    return ret;
  }

  private TrendInfo decodeTrendInfo(ReportLine rl, boolean isMandatory) {
    if (GenericParserHelper.decodeFixedString(rl, "NOSIG")) {
      return TrendInfo.createNOSIG();
    } else {
      List<TrendReport> reports = new ArrayList<>();
      TrendReport report = decodeTrendReport(rl);
      while (report != null) {
        reports.add(report);
        report = decodeTrendReport(rl);
      }
      if (!isMandatory && reports.isEmpty()) {
        return null;
      } else {
        return TrendInfo.create(reports);
      }
    }
  }

  static TrendReport decodeTrendReport(ReportLine rl) {
    TrendReport ret;
    if (GenericParserHelper.decodeFixedString(rl, "BECMG")) {
      ret = new TrendReport();
      ret.setType(TrendReportType.BECMG);

    } else if (GenericParserHelper.decodeFixedString(rl, "TEMPO")) {
      ret = new TrendReport();
      ret.setType(TrendReportType.TEMPO);
    } else {
      return null;
    }

    ret.setTime(GenericParserHelper.decodeTrendReportTime(rl, false));
    ret.setWind(GenericParserHelper.decodeWind(rl, false));
    ret.setVisibility(GenericParserHelper.decodeTrendVisibility(rl, false));
    ret.setPhenomenas(GenericParserHelper.decodeTrendPhenomenas(rl));
    ret.setClouds(GenericParserHelper.decodeTrendCloud(rl));
    return ret;
  }
}
