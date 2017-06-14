package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayStatesInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.RunwayWindshearInfo;
import eng.metarJava.TrendCloudInfo;
import eng.metarJava.TrendPhenomenaInfo;
import eng.metarJava.TrendReportTimeInfo;
import eng.metarJava.TrendVisibilityInfo;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import eng.metarJava.WindInfo;
import eng.metarJava.decoders.exceptions.MissingFieldException;
import eng.metarJava.decoders.fields.ReportField;
import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.enums.TrendReportTimeIndication;
import eng.metarJava.CloudMass;
import eng.metarJava.Report;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.decoders.exceptions.ParseException;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.HourMinute;
import eng.metarJava.enums.PhenomenaIntensity;
import eng.metarJava.enums.PhenomenaType;
import eng.metarJava.support.ReadOnlyList;
import eng.metarJava.support.Speed;
import eng.metarJava.support.Variation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Marek Vajgl
 */
public class GenericParserHelper extends ParserHelper {

  public static ReportType decodeReportType(ReportLine rl) {
    ReportType ret = ReportType.UNKNOWN;
    if (decodeFixedString(rl, "METAR")) {
      ret = ReportType.METAR;
    } else if (decodeFixedString(rl, "SPECI")) {
      ret = ReportType.SPECI;
    }
    return ret;
  }

// <editor-fold defaultstate="collapsed" desc="Report decoding">
  public static boolean decodeFixedString(ReportLine rl, String text) {
    boolean ret = false;
    if (rl.getPre().startsWith(text)) {
      ret = true;
      rl.move(text.length(), true);
    }
    return ret;
  }

  public static boolean decodeCor(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "COR");
    return ret;
  }

  public static String decodeIcao(ReportLine rl) {
    String ret;

    ret = rl.getPre().substring(0, 4);
    rl.move(4, true);
    return ret;
  }

  public static boolean decodeNil(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "NIL");
    return ret;
  }

  public static DayHourMinute decodeDayTime(ReportLine rl) {
    DayHourMinute ret;
    final String regex = "^(\\d{2})(\\d{2})(\\d{2})Z";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());

    if (matcher.find()) {
      int d = groupToInt(matcher.group(1));
      int h = groupToInt(matcher.group(2));
      int m = groupToInt(matcher.group(3));
      ret = new DayHourMinute(d, h, m);
      rl.move(7, true);
    } else {
      throw new MissingFieldException(ReportField.dayTime, rl.getPre(), rl.getPost());
    }

    return ret;
  }

  public static boolean decodeAuto(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "AUTO");
    return ret;
  }

  public static WindInfo decodeWind(ReportLine rl, boolean isMandatory) {
    WindInfo ret;
    final String regexSet = "^(VRB|\\d{3})(\\d{2})(G(\\d{2}))?(KT|KMH|MPS)";

    boolean isUnset = decodeFixedString(rl, "/////KT")
            || decodeFixedString(rl, "/////MPS")
            || decodeFixedString(rl, "/////KMH");
    if (isUnset) {
      ret = null;
    } else {

      final Pattern pattern = Pattern.compile(regexSet);
      final Matcher matcher = pattern.matcher(rl.getPre());

      if (matcher.find()) {
        Heading hdg;
        Speed spd;
        int spdVal;
        Integer gustSpdVal;
        SpeedUnit unit;
        Speed gustSpd;
        boolean isVrb;

        isVrb = matcher.group(1).equals("VRB");
        if (!isVrb) {
          hdg = new Heading(groupToInt(matcher.group(1)));
        } else {
          hdg = null;
        }
        spdVal = groupToInt(matcher.group(2));
        if (groupExist(matcher.group(3))) {
          gustSpdVal = groupToInt(matcher.group(4));
        } else {
          gustSpdVal = null;
        }
        if (matcher.group(5).equals("KT")) {
          unit = SpeedUnit.KT;
        } else if (matcher.group(5).equals("KMH")) {
          unit = SpeedUnit.KMH;
        } else if (matcher.group(5).equals("MPS")) {
          unit = SpeedUnit.MPS;
        } else {
          throw new UnsupportedOperationException();
        }
        rl.move(matcher.group(0).length(), true);

        // to kmh
        spd = new Speed(spdVal, unit);
        if (gustSpdVal != null) {
          gustSpd = new Speed(gustSpdVal, unit);
        } else {
          gustSpd = null;
        }

        Variation<Heading> variations = decodeHeadingVariations(rl);
        ret = WindInfo.createWithOptionals(hdg, spd, gustSpd, variations);
      } else {
        if (isMandatory) {
          throw new MissingFieldException(ReportField.wind, rl.getPre(), rl.getPost());
        } else {
          ret = null;
        }
      }
    }
    return ret;
  }

  public static Variation<Heading> decodeHeadingVariations(ReportLine rl) {
    Variation<Heading> ret = null;
    String regex = "^(\\d{3})V(\\d{3})";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());

    if (matcher.find()) {
      int from = groupToInt(matcher.group(1));
      int to = groupToInt(matcher.group(2));
      ret = new Variation<>(new Heading(from), new Heading(to));

      rl.move(matcher.group(0).length(), true);
    }

    return ret;
  }

  public static PhenomenaInfo decodePhenomena(ReportLine rl) {
    PhenomenaInfo ret;

    String regex = "^(\\+|-|VC)?([A-Z]{2})?([A-Z]{2}(?<!VC))(VC)? ";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      PhenomenaIntensity i = PhenomenaIntensity.moderate;
      List<PhenomenaType> ts = new ArrayList();
      PhenomenaType t;
      boolean isVC = false;
      if (groupExist(matcher.group(1))) {
        if (matcher.group(1).equals("+")) {
          i = PhenomenaIntensity.heavy;
        } else if (matcher.group(1).equals("-")) {
          i = PhenomenaIntensity.light;
        } else if (matcher.group(1).equals("VC")) {
          isVC = true;
        } else {
          throw new UnsupportedOperationException("Unknown phenomena intensity");
        }
      }

      /* 
        Phenomena decoding is tricky as metars do not follow
        standards. Therefore this is very(!) lenient implementation.
       */
      if (groupExist(matcher.group(2))) {
        t = PhenomenaType.valueOf(matcher.group(2));
        ts.add(t);
      }
      t = PhenomenaType.valueOf(matcher.group(3));
      ts.add(t);

      if (groupExist(matcher.group(4))) {
        isVC = true;
      }

      ret = PhenomenaInfo.create(i, ts, isVC);
      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }

    return ret;
  }

  public static CloudInfo decodeCloudsWithVerticalVisibility(ReportLine rl) throws ParseException {
    CloudInfo ret;
    // vertical visibility
    if (decodeFixedString(rl, "VV///")) {
      ret = CloudInfo.createWithUnknownVV();
      rl.move(5, true); // "VV///"
    } else {
      String vvRegex = "^VV(\\d{3})";
      final Pattern vvPattern = Pattern.compile(vvRegex);
      final Matcher matcher = vvPattern.matcher(rl.getPre());
      if (matcher.find()) {
        // is VV
        int vv = groupToInt(matcher.group(1));
        ret = CloudInfo.createWithVV(vv);
        rl.move(matcher.group(0).length(), true);
      } else {
        throw new ParseException(ReportField.clouds, "Seems like [VVxxx] pattern, but does not exactly fit and cannot be parsed.", rl.getPost(), rl.getPre(), null);
      }
    }
    return ret;
  }

  public static List<CloudMass> decodeCloudAmounts(ReportLine rl) {
    List<CloudMass> ret = new ArrayList<>();

    String regex = "^(([A-Z]{3})(\\d{3})(TCU|CB|\\/\\/\\/)?)|(\\/\\/\\/\\/\\/\\/(CB|TCU))";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    while (matcher.find()) {
      if (groupExist(matcher.group(1))) {
        // standard cloud layer mass
        String tmp = matcher.group(2);
        CloudAmount ca = CloudAmount.valueOf(tmp);
        int alt = groupToInt(matcher.group(3));
        CloudMassSignificantFlag flag = CloudMassSignificantFlag.parse(matcher.group(4));
        CloudMass cm = CloudMass.create(ca, alt, flag);
        ret.add(cm);

      } else if (groupExist(matcher.group(5))) {
        // unknown cloud layer mass
        CloudMassSignificantFlag flag = CloudMassSignificantFlag.parse(matcher.group(6));
        CloudMass cm = CloudMass.createWithoutAmountAndBaseHeight(flag);
        ret.add(cm);
      }
      rl.move(matcher.group(0).length(), true);
      matcher = pattern.matcher(rl.getPre());
    }
    return ret;
  }

  /**
   * Returns two element array - first element is temperature, second is dewPoint.
   *
   * @param rl report line
   * @return
   */
  public static TemperatureAndDewPoint decodeTemperatureAndDewPoint(ReportLine rl) {
    TemperatureAndDewPoint ret;

    String regex = "^(M)?(\\d{2})\\/(M)?(\\d{2})";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      int temp = groupToInt(matcher.group(2));
      int dew = groupToInt(matcher.group(4));
      if (groupExist(matcher.group(1))) {
        temp = -temp;
      }
      if (groupExist(matcher.group(3))) {
        dew = -dew;
      }

      ret = new TemperatureAndDewPoint((int) temp, (int) dew);

      rl.move(matcher.group(0).length(), true);
    } else {
      throw new MissingFieldException(ReportField.temperatureDewPoint, rl.getPre(), rl.getPost());
    }
    return ret;
  }

  public static PhenomenaInfo decodeRecentPhenomena(ReportLine rl) {
    PhenomenaInfo ret;

    String regex = "^RE([A-Z]{2})?([A-Z]{2}) ";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    List<PhenomenaType> ts = new ArrayList();
    PhenomenaType t;
    if (matcher.find()) {
      PhenomenaIntensity i = PhenomenaIntensity.moderate;
      if (groupExist(matcher.group(1))) {
        t = PhenomenaType.valueOf(matcher.group(1));
        ts.add(t);
      }
      t = PhenomenaType.valueOf(matcher.group(2));
      ts.add(t);

      // Recent phenomena is expected to never be in-vicinity VC
      ret = PhenomenaInfo.create(i, ts, false);
      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }

    return ret;
  }

  public static RunwayWindshearInfo decodeWindShears(ReportLine rl) {
    RunwayWindshearInfo ret;

    if (decodeFixedString(rl, "WS ALL RWY")) {
      ret = RunwayWindshearInfo.createAllRWY();
    } else {
      List<String> tmp = new ArrayList();
      String regex = "^WS R(\\d{2}[RLC]?)";
      final Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(rl.getPre());
      while (matcher.find()) {
        String r = matcher.group(1);
        tmp.add(r);
        rl.move(matcher.group(0).length(), true);
        matcher = pattern.matcher(rl.getPre());
      }
      ret = RunwayWindshearInfo.create(new ReadOnlyList(tmp));
    }

    return ret;
  }

  /**
   * Skips sea temperature and state. Not important for aviation.
   *
   * @param rl
   */
  public static void decodeSeaTemperatureAndState(ReportLine rl) {
    String regex = "^SM?\\d{2}\\/S\\d{1}";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      rl.move(matcher.group(0).length(), true);
    }
  }

  public static RunwayStatesInfo decodeRunwayStateInfo(ReportLine rl) {
    if (decodeFixedString(rl, "SNOCLO")) {
      return RunwayStatesInfo.createSNOCLO();
    }

    List<RunwayState> rss = new ArrayList();
    String regex = "^R(\\d{2}[RLC]?)\\/([0-9\\/])([0-9\\/])(\\d{2}|\\/\\/)(\\d{2}|\\/\\/)";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    while (matcher.find()) {

      String rwy = matcher.group(1);
      char deposit = matcher.group(2).charAt(0);
      char contamination = matcher.group(3).charAt(0);
      String depth = matcher.group(4);
      String brake = matcher.group(5);

      RunwayState rs = RunwayState.create(rwy, deposit, contamination, depth, brake);
      rss.add(rs);

      rl.move(matcher.group(0).length(), true);
      matcher = pattern.matcher(rl.getPre());
    }
    RunwayStatesInfo ret = RunwayStatesInfo.create(rss);
    return ret;
  }

  public static List<PhenomenaInfo> decodePhenomenas(ReportLine rl) {
    PhenomenaInfo pi = GenericParserHelper.decodePhenomena(rl);
    List<PhenomenaInfo> ret = new ArrayList<>();
    while (pi != null) {
      ret.add(pi);
      pi = GenericParserHelper.decodePhenomena(rl);
    }
    return ret;
  }

  public static List<PhenomenaInfo> decodeRecentPhenomenas(ReportLine rl) {
    PhenomenaInfo pi;
    pi = GenericParserHelper.decodeRecentPhenomena(rl);
    List<PhenomenaInfo> ret = new ArrayList<>();
    while (pi != null) {
      ret.add(pi);
      pi = GenericParserHelper.decodeRecentPhenomena(rl);
    }
    return ret;
  }

  // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Trend report decoding">
  public static TrendReportTimeInfo decodeTrendReportTime(ReportLine rl, boolean isMandatory) {
    TrendReportTimeInfo ret;

    String patternString = "^(AT|FM|TL)(\\d{2})(\\d{2})";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      TrendReportTimeIndication trti;
      int hour, min;
      trti = TrendReportTimeIndication.valueOf(matcher.group(1));
      hour = groupToInt(matcher.group(2));
      min = groupToInt(matcher.group(3));
      ret = new TrendReportTimeInfo(trti, new HourMinute(hour, min));
      rl.move(matcher.group(0).length(), true);
    } else {
      if (isMandatory) {
        throw new MissingFieldException(ReportField.trendTime, rl.getPre(), rl.getPost());
      } else {
        ret = null;
      }
    }

    return ret;
  }

  public static TrendVisibilityInfo decodeTrendVisibility(ReportLine rl, boolean isMandatory) {
    TrendVisibilityInfo ret;

    boolean isCavok = decodeFixedString(rl, "CAVOK");
    if (isCavok) {
      ret = TrendVisibilityInfo.createCAVOK();
    } else {
      String regex = "^\\d{4}";
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(rl.getPre());
      if (matcher.find()) {
        int vis = groupToInt(matcher.group(0));
        rl.move(matcher.group(0).length(), true);
        ret = TrendVisibilityInfo.create(vis);
      } else {
        if (isMandatory) {
          throw new MissingFieldException(ReportField.trendVisibility, rl.getPre(), rl.getPost());
        } else {
          ret = null;
        }
      }
    }

    return ret;
  }

  public static TrendPhenomenaInfo decodeTrendPhenomenas(ReportLine rl) {
    if (decodeFixedString(rl, "NSW")) {
      return TrendPhenomenaInfo.createNSW();
    } else {
      List<PhenomenaInfo> pis = new ArrayList();
      PhenomenaInfo pi = decodePhenomena(rl);
      while (pi != null) {
        pis.add(pi);
        pi = decodePhenomena(rl);
      }

      if (pis.isEmpty()) {
        return TrendPhenomenaInfo.createEmpty();
      } else {
        return TrendPhenomenaInfo.create(pis);
      }
    }
  }

  public static TrendCloudInfo decodeTrendCloud(ReportLine rl) {
    TrendCloudInfo ret;
    if (decodeFixedString(rl, "NSC")) {
      ret = TrendCloudInfo.createNSC();
    } else {
      String vvRegex = "^VV(\\d{3})";
      final Pattern vvPattern = Pattern.compile(vvRegex);
      final Matcher matcher = vvPattern.matcher(rl.getPre());
      if (matcher.find()) {
        // is VV
        int vv = groupToInt(matcher.group(1));
        ret = TrendCloudInfo.createWithVV(vv);
        rl.move(matcher.group(0).length(), true);
      } else {
        List<CloudMass> cls = decodeCloudAmounts(rl);
        ret = TrendCloudInfo.create(cls);
      }
    }
    return ret;
  }

  public static String decodeRemark(ReportLine rl) {
    if (decodeFixedString(rl, "RMK")) {
      String remark = rl.getPre();
      rl.move(remark.length(), true);
      return remark;
    } else {
      return null;
    }
  }
// </editor-fold>
}
