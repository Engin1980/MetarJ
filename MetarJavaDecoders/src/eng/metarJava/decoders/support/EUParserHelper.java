package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.VisibilityVariability;
import eng.metarJava.decoders.exceptions.MissingFieldException;
import static eng.metarJava.decoders.support.GenericParserHelper.decodeFixedString;
import static eng.metarJava.decoders.support.GenericParserHelper.groupToInt;
import eng.metarJava.decoders.support.ReportLine;
import eng.metarJava.enums.Direction;
import eng.metarJava.support.Variation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Marek Vajgl
 */
public class EUParserHelper extends ParserHelper {

  public static VisibilityInfo decodeVisibility(ReportLine rl) {
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

  public static VisibilityVariability decodeVisibilityVariability(ReportLine rl) {
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

  public static int decodePressureInHpa(ReportLine rl) {
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

  public static List<RunwayVisualRange> decodeRunwayVisualRanges(ReportLine rl) {
    RunwayVisualRange rvr;
    List<RunwayVisualRange> lst = new ArrayList();
    rvr = decodeRunwayVisualRange(rl);

    while (rvr != null) {
      lst.add(rvr);
      rvr = decodeRunwayVisualRange(rl);
    }

    return lst;
  }

  public static RunwayVisualRange decodeRunwayVisualRange(ReportLine rl) {
    RunwayVisualRange ret;

    String regex = "^R(\\d{2}[RLC]?)\\/(\\d{4})(V(\\d{4}))?";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(rl.getPre());
    if (matcher.find()) {
      String rwy = matcher.group(1);
      double vis = groupToInt(matcher.group(2));
      Double varVis;
      if (groupExist(matcher.group(3))) {
        varVis = (double) groupToInt(matcher.group(4));
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
  
  

  public static CloudInfo decodeClouds(ReportLine rl) {
    CloudInfo ret;
    if (decodeFixedString(rl, "NSC")) {
      ret = CloudInfo.createNSC();
    } else if (decodeFixedString(rl, "NCD")) {
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
