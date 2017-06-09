package eng.metarJava;

import eng.metarJava.enums.TrendReportTimeIndication;
import eng.metarJava.enums.TrendReportType;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.HourMinute;

/**
 * Represents one trend message different from NOSIG in the report. Message is introduced by TEMPO/BECMG keywords. Messages are
 * captured in {@linkplain TrendInfo} instance (see {@link TrendInfo#getTrends() method), which is in {@linkplain Report}.
 *
 * @author Marek Vajgl
 */
public class TrendReport {

  private TrendReportType type;

  /**
   * Represents trend type.
   *
   * @return
   */
  public TrendReportType getType() {
    return type;
  }

  /**
   * See {@linkplain #getType }.
   *
   * @param type
   */
  public void setType(TrendReportType type) {
    this.type = type;
  }

  private TrendReportTimeInfo time = null;

  /**
   * Represents time at which the trend report occurs. Consists of from FM / at AT/ until TL and hour+minute of occurence. See
   * {@linkplain  TrendReportTimeInfo} for more info. If null, no time is specified. Optional. Default value is null.
   *
   * @return
   */
  public TrendReportTimeInfo getTime() {
    return time;
  }

  /**
   * See {@linkplain #getTime() }.
   *
   * @param time
   */
  public void setTime(TrendReportTimeInfo time) {
    this.time = time;
  }

  private WindInfo wind = null;

  /**
   * Returns wind trend info. Null if no wind is reported. Optional. Default value is null.
   *
   * @return
   */
  public WindInfo getWind() {
    return wind;
  }

  /**
   * See {@linkplain #getWind() }.
   *
   * @param wind
   */
  public void setWind(WindInfo wind) {
    this.wind = wind;
  }

  private TrendVisibilityInfo visibility = null;

  /**
   * Represents visibility info. Trend visibility cannot be NDV. Optional. Default value is null.
   *
   * @return
   */
  public TrendVisibilityInfo getVisibility() {
    return visibility;
  }

  /**
   * See {@linkplain #getVisibility() }.
   *
   * @param visibility
   */
  public void setVisibility(TrendVisibilityInfo visibility) {
    this.visibility = visibility;
  }

  private TrendPhenomenaInfo phenomenas = null;

  /**
   * Represents trend phenomena. Moreover, trend phenomena can be set at NSW - no significant weather flag. If null, no phenomena info
   * is reported. Optional. Default value is null.
   *
   * @return
   */
  public TrendPhenomenaInfo getPhenomenas() {
    return phenomenas;
  }

  /**
   * See {@linkplain #getPhenomenas() }.
   *
   * @param phenomenas
   */
  public void setPhenomenas(TrendPhenomenaInfo phenomenas) {
    this.phenomenas = phenomenas;
  }

  private TrendCloudInfo cloudInfo = null;

  /**
   * Represents trend cloud info. Instead of common cloud, trend cannot be NCD - no clouds detected. Optional. Default value is null.
   *
   * @return
   */
  public TrendCloudInfo getCloudInfo() {
    return cloudInfo;
  }

  /**
   * See {@linkplain #getCloudInfo() }.
   *
   * @param cloudInfo
   */
  public void setCloudInfo(TrendCloudInfo cloudInfo) {
    this.cloudInfo = cloudInfo;
  }

}
