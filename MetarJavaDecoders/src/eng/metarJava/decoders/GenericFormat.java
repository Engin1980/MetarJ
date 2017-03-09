package eng.metarJava.decoders;

import eng.metarJava.Report;

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
    
    return ret;
  }
  
}
