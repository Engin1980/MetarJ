package eng.metarJava.decoders;

import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import eng.metarJava.WindInfo;
import eng.metarJava.decoders.exceptions.MissingFieldException;
import eng.metarJava.decoders.exceptions.ParseException;
import eng.metarJava.decoders.fields.ReportField;
import eng.metarJava.enums.Direction;
import eng.metarJava.enums.ReportType;
import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.support.DayHourMinute;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Variation;
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

  private static boolean decodeFixedString(ReportLine rl, String text) {
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

  static WindInfo decodeWind(ReportLine rl) {
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
        int spd;
        SpeedUnit unit;
        Integer gustSpd;
        boolean isVrb;

        isVrb = matcher.group(1).equals("VRB");
        if (!isVrb) {
          hdg = new Heading(groupToInt(matcher.group(1)));
        } else {
          hdg = null;
        }
        spd = groupToInt(matcher.group(2));
        if (groupExist(matcher.group(3))) {
          gustSpd = groupToInt(matcher.group(4));
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

        Variation<Heading> variations = decodeHeadingVariations(rl);
        ret = new WindInfo(hdg, spd, gustSpd, unit, variations);
      } else {
        throw new MissingFieldException(ReportField.wind, rl.getPre(), rl.getPost());
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
      ret = new VisibilityInfo(null, false, null);
    } else {
      String regex = "^(\\d{4})(NDV)?";
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(rl.getPre());
      if (matcher.find()) {
        int vis = groupToInt(matcher.group(1));
        boolean isNVD = groupExist(matcher.group(2));
        rl.move(matcher.group(0).length(), true);
        VisibilityVariability var = decodeVisibilityVariability(rl);
        ret = new VisibilityInfo(vis, isNVD, var);
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
      ret = new VisibilityVariability(vis, d);

      rl.move(matcher.group(0).length(), true);
    }
    return ret;
  }

  static RunwayVisualRange decodeRunwayVisualRange(ReportLine rl) {
    RunwayVisualRange ret;

    String regex = "R(\\d{2}[RLC]?)\\/(\\d{4})(V(\\d{4}))?";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      String rwy = matcher.group(1);
      int vis = groupToInt(matcher.group(2));
      Integer varVis;
      if (groupExist(matcher.group(3))){
        varVis = groupToInt(matcher.group(4));
      } else {
        varVis = null;
      }
      if (varVis == null)
        ret = new RunwayVisualRange(rwy, vis);
      else
        ret = new RunwayVisualRange(rwy, new Variation<>(vis, varVis));

      rl.move(matcher.group(0).length(), true);
    } else{
      ret = null;
    }
    return ret;
  }

}
