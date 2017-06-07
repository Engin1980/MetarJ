package eng.metarJava.decoders;

import eng.metarJava.CloudInfo;
import eng.metarJava.PhenomenaInfo;
import eng.metarJava.RunwayState;
import eng.metarJava.RunwayStatesInfo;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.RunwayWindshearInfo;
import eng.metarJava.TrendCloudInfo;
import eng.metarJava.TrendInfo;
import eng.metarJava.TrendPhenomenaInfo;
import eng.metarJava.TrendReport;
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
import eng.metarJava.enums.TrendReportType;
import eng.metarJava.CloudMass;
import eng.metarJava.decoders.exceptions.ParseException;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.HourMinute;
import eng.metarJava.support.PhenomenaDescriptor;
import eng.metarJava.support.PhenomenaIntensity;
import eng.metarJava.support.PhenomenaType;
import eng.metarJava.support.TryResult;
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
class SharedParse {

  static ReportType decodeReportType(ReportLine rl) {
    ReportType ret = ReportType.UNKNOWN;
    if (decodeFixedString(rl, "METAR")) {
      ret = ReportType.METAR;
    } else if (decodeFixedString(rl, "SPECI")) {
      ret = ReportType.SPECI;
    }
    return ret;
  }

// <editor-fold defaultstate="collapsed" desc="Report decoding">
  static boolean decodeFixedString(ReportLine rl, String text) {
    boolean ret = false;
    if (rl.getPre().startsWith(text)) {
      ret = true;
      rl.move(text.length(), true);
    }
    return ret;
  }

  static boolean decodeCor(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "COR");
    return ret;
  }

  static String decodeIcao(ReportLine rl) {
    String ret;

    ret = rl.getPre().substring(0, 4);
    rl.move(4, true);
    return ret;
  }

  static boolean decodeNil(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "NIL");
    return ret;
  }

  static DayHourMinute decodeDayTime(ReportLine rl) {
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

  static int groupToInt(String txt) {
    int ret = Integer.parseInt(txt);
    return ret;
  }

  static boolean decodeAuto(ReportLine rl) {
    boolean ret = decodeFixedString(rl, "AUTO");
    return ret;
  }

  static WindInfo decodeWind(ReportLine rl, boolean isMandatory) {
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
        double spd;
        SpeedUnit unit;
        Double gustSpd;
        boolean isVrb;

        isVrb = matcher.group(1).equals("VRB");
        if (!isVrb) {
          hdg = new Heading(groupToInt(matcher.group(1)));
        } else {
          hdg = null;
        }
        spd = groupToInt(matcher.group(2));
        if (groupExist(matcher.group(3))) {
          gustSpd = (double) groupToInt(matcher.group(4));
        } else {
          gustSpd = null;
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
        spd = SpeedUnit.convert(spd, unit, SpeedUnit.KMH);
        gustSpd = SpeedUnit.convert(gustSpd, unit, SpeedUnit.KMH);

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

  private static boolean groupExist(String groupText) {
    return groupText != null;
  }

  private static Variation<Heading> decodeHeadingVariations(ReportLine rl) {
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

  static VisibilityInfo decodeVisibility(ReportLine rl) {
    VisibilityInfo ret;

    boolean isCavok = decodeFixedString(rl, "CAVOK");
    if (isCavok) {
      ret = VisibilityInfo.createCAVOK();
    } else {
      String regex = "^(\\d{4})(NDV)?";
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(rl.getPre());
      if (matcher.find()) {
        int vis = groupToInt(matcher.group(1));
        boolean isNVD = groupExist(matcher.group(2));
        rl.move(matcher.group(0).length(), true);
        VisibilityVariability var = decodeVisibilityVariability(rl);
        if (isNVD) {
          ret = VisibilityInfo.createWithNDV(vis);
        } else {
          ret = VisibilityInfo.create(vis, var);
        }
      } else {
        throw new MissingFieldException(ReportField.visibility, rl.getPre(), rl.getPost());
      }
    }

    return ret;
  }

  private static VisibilityVariability decodeVisibilityVariability(ReportLine rl) {
    VisibilityVariability ret = null;

    String regex = "^(\\d{4})([NSEW])";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      int vis = groupToInt(matcher.group(1));
      String dir = matcher.group(2);
      Direction d = Direction.parse(dir.charAt(0));
      ret = VisibilityVariability.create(vis, d);

      rl.move(matcher.group(0).length(), true);
    }
    return ret;
  }

  static RunwayVisualRange decodeRunwayVisualRange(ReportLine rl) {
    RunwayVisualRange ret;

    String regex = "^R(\\d{2}[RLC]?)\\/(\\d{4})(V(\\d{4}))?";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      String rwy = matcher.group(1);
      int vis = groupToInt(matcher.group(2));
      Integer varVis;
      if (groupExist(matcher.group(3))) {
        varVis = groupToInt(matcher.group(4));
      } else {
        varVis = null;
      }
      if (varVis == null) {
        ret = RunwayVisualRange.create(rwy, vis);
      } else {
        ret = RunwayVisualRange.create(rwy, new Variation<>(vis, varVis));
      }

      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }
    return ret;
  }

  static PhenomenaInfo decodePhenomena(ReportLine rl) {
    PhenomenaInfo ret;

    String regex = "^(\\+|-|VC)?([A-Z]{2})?([A-Z]{2}) ";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      PhenomenaIntensity i = PhenomenaIntensity.moderate;
      List<PhenomenaType> ts = new ArrayList();
      PhenomenaType t;
      if (groupExist(matcher.group(1))) {
        if (matcher.group(1).equals("+")) {
          i = PhenomenaIntensity.heavy;
        } else if (matcher.group(1).equals("-")) {
          i = PhenomenaIntensity.light;
        } else if (matcher.group(1).equals("VC")) {
          i = PhenomenaIntensity.inVicinity;
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

      ret = PhenomenaInfo.create(i, ts);
      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }

    /* OLD without vicinity in front of phenomenas
    String regex = "^([+-])?([A-Z]{2})?([A-Z]{2}(?<!VC))(VC)? ";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      PhenomenaIntensity i = PhenomenaIntensity.moderate;
      if (groupExist(matcher.group(1))) {
        if (matcher.group(1).equals("+")) {
          i = PhenomenaIntensity.heavy;
        } else if (matcher.group(1).equals("-")) {
          i = PhenomenaIntensity.light;
        } else {
          throw new UnsupportedOperationException("Unknown phenomena intensity");
        }
      }
      PhenomenaDescriptor d = PhenomenaDescriptor.none;
      if (groupExist(matcher.group(2))) {
        d = PhenomenaDescriptor.valueOf(matcher.group(2));
      }
      PhenomenaType t = PhenomenaType.valueOf(matcher.group(3));
      boolean isVC = groupExist(matcher.group(4));

      ret = PhenomenaInfo.create(i, d, t, isVC);
      rl.move(matcher.group(0).length(), isVC);
    } else {
      ret = null;
    }
     */
    return ret;
  }

  static CloudInfo decodeClouds(ReportLine rl) {
    CloudInfo ret;
    if (decodeFixedString(rl, "NSC")) {
      ret = CloudInfo.createNSC();
    } else if (decodeFixedString(rl, "NCD")) {
      ret = CloudInfo.createNCD();
    } else if (rl.getPre().startsWith("VV")) {
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
    } else {
      // cloud masses defined somehow
      List<CloudMass> cls = decodeCloudAmounts(rl);
      ret = CloudInfo.create(cls);
    }
    return ret;
  }

  private static List<CloudMass> decodeCloudAmounts(ReportLine rl) {
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
  static Integer[] decodeTemperatureAndDewPoint(ReportLine rl) {
    Integer[] ret;

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

      ret = new Integer[2];
      ret[0] = temp;
      ret[1] = dew;

      rl.move(matcher.group(0).length(), true);
    } else {
      throw new MissingFieldException(ReportField.temperatureDewPoint, rl.getPre(), rl.getPost());
    }
    return ret;
  }

  static int decodePressureInHpa(ReportLine rl) {
    int ret;
    String regex = "^Q(\\d{4})";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      ret = groupToInt(matcher.group(1));
      rl.move(matcher.group(0).length(), true);
    } else {
      throw new MissingFieldException(ReportField.pressure, rl.getPre(), rl.getPost());
    }
    return ret;
  }

  static PhenomenaInfo decodeRecentPhenomena(ReportLine rl) {
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

      ret = PhenomenaInfo.create(i, ts);
      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }

    return ret;
  }

  static RunwayWindshearInfo decodeWindShears(ReportLine rl) {
    RunwayWindshearInfo ret;

    if (decodeFixedString(rl, "WS ALL RWY")) {
      ret = RunwayWindshearInfo.createAllRWY();
    } else {
      Set<String> tmp = new HashSet();
      String regex = "^WS R(\\d{2}[RLC]?)";
      final Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(rl.getPre());
      if (matcher.find()) {
        String r = matcher.group(1);
        tmp.add(r);
        rl.move(matcher.group(0).length(), true);
        matcher = pattern.matcher(rl.getPre());
      }
      ret = RunwayWindshearInfo.create(tmp);
    }

    return ret;
  }

  /**
   * Skips sea temperature and state. Not important for aviation.
   *
   * @param rl
   */
  static void decodeSeaTemperatureAndState(ReportLine rl) {
    String regex = "^SM?\\d{2}\\/S\\d{1}";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      rl.move(matcher.group(0).length(), true);
    }
  }

  static RunwayStatesInfo decodeRunwayStateInfo(ReportLine rl) {
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

  // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Trend report decoding">
  static TrendReportTimeInfo decodeTrendReportTime(ReportLine rl, boolean isMandatory) {
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

  static TrendVisibilityInfo decodeTrendVisibility(ReportLine rl, boolean isMandatory) {
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

  static TrendPhenomenaInfo decodeTrendPhenomenas(ReportLine rl) {
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

  static TrendCloudInfo decodeTrendCloud(ReportLine rl) {
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

  static String decodeRemark(ReportLine rl) {
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
