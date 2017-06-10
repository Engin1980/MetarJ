package eng.metarJava.decoders;

import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.TrendInfo;
import eng.metarJava.TrendReport;
import eng.metarJava.enums.TrendReportType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class GenericParser implements Parser {

  @Override
  public Report parse(String line) {
    ReportLine rl = new ReportLine(line);
    Report ret = new Report();

    ret.setType(SharedParse.decodeReportType(rl));
    ret.setCorrection(SharedParse.decodeCor(rl));
    ret.setIcao(SharedParse.decodeIcao(rl));
    ret.setDayTime(SharedParse.decodeDayTime(rl));
    ret.setNil(SharedParse.decodeNil(rl));
    if (ret.isNil()) {
      return ret;
    }

    ret.setAuto(SharedParse.decodeAuto(rl));
    ret.setWind(SharedParse.decodeWind(rl, true));
    ret.setVisibility(SharedParse.decodeVisibility(rl));
    decodeRunwayVisualRanges(ret, rl);
    decodePhenomenas(ret, rl);
    ret.setClouds(SharedParse.decodeClouds(rl));
    decodeTempAndDew(ret, rl);
    ret.setPressureInHpa(SharedParse.decodePressureInHpa(rl));
    decodeRecentPhenomenas(ret, rl);
    ret.setRunwayWindshears(SharedParse.decodeWindShears(rl));
    SharedParse.decodeSeaTemperatureAndState(rl); // not valid for aviation
    ret.setRunwayStatesInfo(SharedParse.decodeRunwayStateInfo(rl));
    ret.setTrendInfo(decodeTrendInfo(rl, false));
    ret.setRemark(SharedParse.decodeRemark(rl));

    return ret;
  }

  private void decodeRunwayVisualRanges(Report ret, ReportLine rl) {
    RunwayVisualRange rvr;
    rvr = SharedParse.decodeRunwayVisualRange(rl);
    while (rvr != null) {
      ret.getRunwayVisualRanges().add(rvr);
      rvr = SharedParse.decodeRunwayVisualRange(rl);
    }
  }

  private void decodePhenomenas(Report ret, ReportLine rl) {
    PhenomenaInfo pi;

    pi = SharedParse.decodePhenomena(rl);
    while (pi != null) {
      ret.getPhenomenas().add(pi);
      pi = SharedParse.decodePhenomena(rl);
    }
  }

  private void decodeTempAndDew(Report ret, ReportLine rl) {
    Integer[] tmp = SharedParse.decodeTemperatureAndDewPoint(rl);

    ret.setTemperature(tmp[0]);
    ret.setDewPoint(tmp[1]);
  }

  private void decodeRecentPhenomenas(Report ret, ReportLine rl) {
    PhenomenaInfo pi;

    pi = SharedParse.decodeRecentPhenomena(rl);
    while (pi != null) {
      ret.getRecentPhenomenas().add(pi);
      pi = SharedParse.decodeRecentPhenomena(rl);
    }
  }

  private TrendInfo decodeTrendInfo(ReportLine rl, boolean isMandatory) {
    if (SharedParse.decodeFixedString(rl, "NOSIG")) {
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
    if (SharedParse.decodeFixedString(rl, "BECMG")) {
      ret = new TrendReport();
      ret.setType(TrendReportType.BECMG);

    } else if (SharedParse.decodeFixedString(rl, "TEMPO")) {
      ret = new TrendReport();
      ret.setType(TrendReportType.TEMPO);
    } else {
      return null;
    }

    ret.setTime(SharedParse.decodeTrendReportTime(rl, false));
    ret.setWind(SharedParse.decodeWind(rl, false));
    ret.setVisibility(SharedParse.decodeTrendVisibility(rl, false));
    ret.setPhenomenas(SharedParse.decodeTrendPhenomenas(rl));
    ret.setClouds(SharedParse.decodeTrendCloud(rl));
    return ret;
  }
}
