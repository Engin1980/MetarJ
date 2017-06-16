package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.decoders.support.ParserHelper;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import static eng.metarJava.decoders.support.ParserHelper.groupExist;
import static eng.metarJava.decoders.support.ParserHelper.groupToInt;
import eng.metarJava.decoders.exceptions.MissingFieldException;
import eng.metarJava.decoders.exceptions.ParseException;
import static eng.metarJava.decoders.support.GenericParserHelper.decodeFixedString;
import static eng.metarJava.decoders.support.ParserHelper.groupExist;
import static eng.metarJava.decoders.support.ParserHelper.groupToInt;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.enums.PressureUnit;
import eng.metarJava.support.Variation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Marek Vajgl
 */
public class USParserHelper extends ParserHelper {

  public static VisibilityInfo decodeVisibility(ReportLine rl) {
    VisibilityInfo ret;

    String regex = "((\\d{1,2})SM)|((\\d{1,2})\\/(\\d{1,2})SM)|((\\d{1,2}) (\\d{1,2})\\/(\\d{1,2})SM)";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    double visInSm = 0;
    if (matcher.find()) {
      if (groupExist(matcher.group(1))) {
        // int number
        visInSm = groupToInt(matcher.group(2));
      } else if (groupExist(matcher.group(3))) {
        // only rational number
        int nom = groupToInt(matcher.group(4));
        int den = groupToInt(matcher.group(5));
        visInSm = nom / (double) den;
      } else if (groupExist(matcher.group(6))) {
        // int followed by rational number
        visInSm = groupToInt(matcher.group(7));
        int nom = groupToInt(matcher.group(8));
        int den = groupToInt(matcher.group(9));
        visInSm += nom / (double) den;
      } else {
        throw new ParseException(
                ReportField.visibility, "Unable to decode visibility from " + matcher.group(0),
                rl.getPost(), rl.getPre(), null);
      }
      rl.move(matcher.group(0).length(), true);
      ret = VisibilityInfo.create(visInSm, DistanceUnit.miles);
    } else {
      ret = VisibilityInfo.createCAVOK();
    }

    return ret;
  }

  public static double decodePressureInInchesHg(ReportLine rl) {
    double ret;
    String regex = "^A(\\d{4})";
    final Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      ret = groupToInt(matcher.group(1));
      ret = ret / 100d;
      rl.move(matcher.group(0).length(), true);
    } else {
      throw new MissingFieldException(ReportField.pressure, rl.getPre(), rl.getPost());
    }
    return ret;
  }

  public static List<RunwayVisualRange> decodeRunwayVisualRanges(ReportLine rl) {
    RunwayVisualRange rvr;
    List<RunwayVisualRange> lst = new ArrayList();
    rvr = USParserHelper.decodeRunwayVisualRange(rl);

    while (rvr != null) {
      lst.add(rvr);
      rvr = USParserHelper.decodeRunwayVisualRange(rl);
    }

    return lst;
  }

  public static RunwayVisualRange decodeRunwayVisualRange(ReportLine rl) {
    RunwayVisualRange ret;

    String regex = "^R(\\d{2}[RLC]?)\\/(M)?(\\d{4})(V(P)?(\\d{4}))?FT";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      String rwy = matcher.group(1);
      boolean isUnderMinimal = groupExist(matcher.group(2));
      double vis = groupToInt(matcher.group(3));
      Double varVis;
      boolean isOverMaximal = false;
      if (groupExist(matcher.group(4))) {
        isOverMaximal = groupExist(matcher.group(5));
        varVis = (double) groupToInt(matcher.group(6));
      } else {
        varVis = null;
      }

      if (isUnderMinimal) {
        vis = 900d; // minimal value is 1000;
      }
      if (varVis == null) {
        ret = RunwayVisualRange.create(rwy, vis, DistanceUnit.feet);
      } else {
        if (isOverMaximal) {
          varVis = 7000d; // maximal value is 6000;
        }
        ret = RunwayVisualRange.create(rwy, new Variation<>(vis, varVis), DistanceUnit.feet);
      }

      rl.move(matcher.group(0).length(), true);
    } else {
      ret = null;
    }
    return ret;
  }

  public static CloudInfo decodeClouds(ReportLine rl) {
    CloudInfo ret;
    if (decodeFixedString(rl, "NSC")) {
      ret = CloudInfo.createNSC();
    } else if (decodeFixedString(rl, "NCD") || decodeFixedString(rl, "CLR")) {
      ret = CloudInfo.createNCD();
    } else if (rl.getPre().startsWith("VV")) {
      ret = GenericParserHelper.decodeCloudsWithVerticalVisibility(rl);
    } else {
      // cloud masses defined somehow
      List<CloudMass> cls = GenericParserHelper.decodeCloudAmounts(rl);
      ret = CloudInfo.create(cls);
    }
    return ret;
  }
}
