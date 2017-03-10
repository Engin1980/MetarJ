package eng.metarJava.decoders;

import eng.metarJava.PhenomenaInfo;
import eng.metarJava.Report;
import eng.metarJava.RunwayVisualRange;

/**
 *
 * @author Marek Vajgl
 */
public class GenericFormat implements IParse {
  
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
    ret.setWind(SharedParse.decodeWind(rl));
    ret.setVisibility(SharedParse.decodeVisibility(rl));
    decodeRunwayVisualRanges(ret, rl);
    decodePhenomenas(ret, rl);
    ret.setClouds(SharedParse.decodeClouds(rl));
    decodeTempAndDew(ret, rl);
    ret.setPressureInHpa(SharedParse.decodePressureInHpa(rl));
    decodeRecentPhenomenas(ret, rl);
    ret.setRunwayWindshears(SharedParse.decodeWindShears(rl));
    SharedParse.decodeSeaTemperatureAndState(rl); // not valid for aviation
    ret.setRunwayStateInfo(SharedParse.decodeRunwayStateInfo(rl));
    ret.setTrendInfo(SharedParse.decodeTrendInfo(rl));
    
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
}
