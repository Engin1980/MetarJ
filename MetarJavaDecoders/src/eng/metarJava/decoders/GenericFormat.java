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
    if (ret.isNil()) return ret;
    
    ret.setAuto(SharedParse.decodeAuto(rl));
    ret.setWind(SharedParse.decodeWind(rl));
    ret.setVisibility(SharedParse.decodeVisibility(rl));
    decodeRunwayVisualRanges(ret, rl);
    decodePhenomenas(ret, rl);
    
    return ret;
  }

  private void decodeRunwayVisualRanges(Report ret, ReportLine rl) {
    RunwayVisualRange rvr;
    rvr = SharedParse.decodeRunwayVisualRange(rl);
    while (rvr != null){
      ret.getRunwayVisualRanges().add(rvr);
      rvr = SharedParse.decodeRunwayVisualRange(rl);
    }
  }

  private void decodePhenomenas(Report ret, ReportLine rl) {
    PhenomenaInfo pi;
    
    pi = SharedParse.decodePhenomena(rl);
    while (pi != null){
      ret.getPhenomenas().add(pi);
      pi = SharedParse.decodePhenomena(rl);
    }
  }
}
