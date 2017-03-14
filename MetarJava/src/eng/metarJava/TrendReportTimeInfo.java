package eng.metarJava;

import eng.metarJava.enums.TrendReportTimeIndication;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.HourMinute;

/**
 *
 * @author Marek Vajgl
 */
public class TrendReportTimeInfo {
  private final TrendReportTimeIndication indication;
  private final HourMinute time;

  public TrendReportTimeInfo(TrendReportTimeIndication indication, HourMinute time) {
    if (time == null)
      throw new NullArgumentException("[time]");
    
    this.indication = indication;
    this.time = time;
  }

  public TrendReportTimeIndication getIndication() {
    return indication;
  }

  public HourMinute getTime() {
    return time;
  }
}
