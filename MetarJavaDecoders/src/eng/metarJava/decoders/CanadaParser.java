package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.Report;
import eng.metarJava.decoders.support.EUParserHelper;
import eng.metarJava.decoders.support.GenericParserHelper;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.decoders.support.TemperatureAndDewPoint;
import eng.metarJava.decoders.support.USParserHelper;
import eng.metarJava.enums.PressureUnit;

/**
 * Parses text report into an instance of {@linkplain eng.metarJava.Report } using Canada format. To parse a report,
 * use {@linkplain #parse(java.lang.String) } method. <br><br>
 * Specific info: very similar to {@link eng.metarJava.USParser US parser}, see its description for details. Differences
 * from US parser:<br>
 * <ul>
 * <li></li>
 * <li>Pressure should be in hPa according to the specification, however in real life inHq is used, so this parser uses it too.</li>
 * <li>Clouds can be defined as CLR what means "no clouds detected" ({@link eng.metarJava.CloudInfo#isNoDetected() NCD}) when no clouds
 * are detected below FL100 on automated stations, or SKC when there are "no significant clouds" ({@link eng.metarJava.CloudInfo#isNSC() NSC}).
 * </ul>
 * @author Marek Vajgl
 */
public class CanadaParser implements Parser {

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
    ret.setVisibility(USParserHelper.decodeVisibility(rl));
    ret.setRunwayVisualRanges(USParserHelper.decodeRunwayVisualRanges(rl));
    ret.setPhenomenas(GenericParserHelper.decodePhenomenas(rl));
    
    // specific
    decodeClouds(rl, ret);    
    
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

  private void decodeClouds(ReportLine rl, Report ret) {
    CloudInfo ci;
    if (rl.getPre().startsWith("SKC"))
    {
      ci = CloudInfo.createAsNoSignificant();
      rl.move(3, true);
    } else {
      ci = USParserHelper.decodeClouds(rl);
    }
    ret.setClouds(ci);
  }
  
}
