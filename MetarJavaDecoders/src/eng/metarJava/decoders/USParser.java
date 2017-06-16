package eng.metarJava.decoders;

import eng.metarJava.decoders.support.USParserHelper;
import eng.metarJava.Report;
import eng.metarJava.decoders.support.GenericParserHelper;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.decoders.support.TemperatureAndDewPoint;
import eng.metarJava.enums.PressureUnit;

/**
 * Parses text report into an instance of {@linkplain eng.metarJava.Report } using US format. To parse a report,
 * use {@linkplain #parse(java.lang.String) } method. <br><br>
 * Specific info:<br>
 * <ul>
 * <li>{@link eng.metarJava.Report#getType() Type} prefix is mandatory.</li>
 * <li>{@link eng.metarJava.Report#getWind() Wind} is optional a can be null.</li>
 * <li>{@link eng.metarJava.Report#getVisibility() Visibility } uses statute miles.</li>
 * <li>{@link eng.metarJava.Report#getRunwayVisualRanges()  Runway-visual-range } uses feet as an distnace. It is limited to minimal 1000FT (represented as M1000 in the report) and maxima of 6000FT
 * (represented as P6000FT in the report).</li>
 * <li>{@link eng.metarJava.Report#getClouds() Clouds } cannot have CAVOK, have CLR instead of NCD. Can have unlimited number of cloud layers.</li>
 * <li>{@link eng.metarJava.Report#getPressure(eng.metarJava.enums.PressureUnit) Pressure } is in inHg</li>
 * </ul>
 * @author Marek Vajgl
 */
public class USParser implements Parser {
  
  private final boolean strict;

  public USParser(boolean strict) {
    this.strict = strict;
  }

  public USParser() {
    this(false);
  }

  public boolean isStrict() {
    return strict;
  }
  
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
    ret.setWind(GenericParserHelper.decodeWind(rl, strict));
    ret.setVisibility(USParserHelper.decodeVisibility(rl));
    ret.setRunwayVisualRanges(USParserHelper.decodeRunwayVisualRanges(rl));
    ret.setPhenomenas(GenericParserHelper.decodePhenomenas(rl));
    ret.setClouds(USParserHelper.decodeClouds(rl));
    TemperatureAndDewPoint tdp = GenericParserHelper.decodeTemperatureAndDewPoint(rl);
    ret.setTemperature(tdp.temperature);
    ret.setDewPoint(tdp.dewPoint);
    ret.setPressure(USParserHelper.decodePressureInInchesHg(rl), PressureUnit.inHq);
    ret.setRecentPhenomenas(GenericParserHelper.decodeRecentPhenomenas(rl));
    ret.setRunwayWindshears(GenericParserHelper.decodeWindShears(rl));
    GenericParserHelper.decodeSeaTemperatureAndState(rl); // not valid for aviation
    ret.setRunwayStatesInfo(GenericParserHelper.decodeRunwayStateInfo(rl));
    
    // well, according to specification US metars do not have trends 
    //ret.setTrendInfo(decodeTrendInfo(rl, false));
    
    ret.setRemark(GenericParserHelper.decodeRemark(rl));

    return ret;
  }
}
