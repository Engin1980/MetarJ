package eng.metarJava;

import eng.metarJava.enums.TrendReportTimeIndication;
import eng.metarJava.exceptions.NullArgumentException;
import eng.metarJava.support.HourMinute;

/**
 *
 * @author Marek Vajgl
 */
public class TrendReportTimeInfo {
  private final TrendReportTimeIndication indication;
  private final HourMinute time;
  
  /**
   * Creates a new instance. Both parameters are mandatory.
   * @param indication indication of AT/FM/TL flag
   * @param time time of occurence in HHMM
   * @throws NullArgumentException when time is null
   */
  public static TrendReportTimeInfo create(TrendReportTimeIndication indication, HourMinute time){
    TrendReportTimeInfo ret = new TrendReportTimeInfo(indication, time);
    return ret;
  }

  /**
   * Creates a new instance.
   * @param indication indication of AT/FM/TL flag
   * @param time time of occurence in HHMM
   * @throws NullArgumentException when time is null
   */
  public TrendReportTimeInfo(TrendReportTimeIndication indication, HourMinute time) {
    if (time == null)
      throw new NullArgumentException("time");
    
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
