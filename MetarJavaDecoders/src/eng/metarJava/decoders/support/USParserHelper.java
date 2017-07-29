package eng.metarJava.decoders.support;

import eng.metarJava.CloudInfo;
import eng.metarJava.CloudMass;
import eng.metarJava.RunwayVisualRange;
import eng.metarJava.VisibilityInfo;
import eng.metarJava.decoders.exceptions.MissingFieldException;
import eng.metarJava.decoders.exceptions.ParseException;
import static eng.metarJava.decoders.support.GenericParserHelper.decodeFixedString;
import static eng.metarJava.decoders.support.ParserHelper.groupExist;
import static eng.metarJava.decoders.support.ParserHelper.groupToInt;
import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.support.Variation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * US parser helper containing methods used to decode METAR according to the US standards.
 * @author Marek Vajgl
 */
public class USParserHelper extends ParserHelper {

  /**
   * Decodes visibility in fractions of statual miles.
   * @param rl Report line with cursor before visibility position.
   * @return VisibilityInfo
   */
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

  /**
   * Decodes pressure in inches of Hg
   * @param rl Report line with cursor before pressure position.
   * @return Value of pressure in inHg
   */
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

  /**
   * Decodes runway visual ranges (if exist). Never returns null.
   * @param rl Report line with the cursor before runway visual range definition (if exist)
   * @return List of decoded visual ranges. If no runway visual range specified, empty list is returned.
   */
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

  /**
   * Decodes one ruwnay visual range. If no range found, returns null.<br>
   * Decoding is done according to the US standards. Minimal value is 1000FT, maximal value is 6000FT. 
   * @param rl Report line with the curosr before runway visual range definition (if exist)
   * @return Instace of {@linkplain RunwayVisualRange} if exist.
   */
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

  /**
   * Decodes clouds from report line<br>
   * Possibilities:<br>
   * <ul>
   * <li>If empty, means no significant clouds are detected.</li>
   * <li>If CLR, means no clouds detected below FL120.</li>
   * <li>If VV..., means vertical visibility is announced.</li>
   * <li>If OVC..., etc., means cloud layers are specified.</li>
   * </ul>
   * @param rl
   * @return Returns instance of cloud info. See method description for further info.
   */
  public static CloudInfo decodeClouds(ReportLine rl) {
    CloudInfo ret;
    if (decodeFixedString(rl, "CLR")) {
      ret = CloudInfo.createAsNoDetected();
    } else if (rl.getPre().startsWith("VV")) {
      ret = GenericParserHelper.decodeCloudsWithVerticalVisibility(rl);
    } else {
      // cloud masses defined somehow
      List<CloudMass> cls = GenericParserHelper.decodeCloudAmounts(rl);

      if (cls.size() == 0) {
        ret = CloudInfo.createAsNoSignificant();
      } else {
        ret = CloudInfo.create(cls);
      }
    }
    return ret;
  }
}
