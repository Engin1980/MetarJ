package eng.metarJava;

import eng.metarJava.enums.TrendReportType;
import eng.metarJava.exception.NullArgumentException;

/**
 *
 * @author Marek Vajgl
 */
public class TrendReport {
  private TrendReportType type;
  public TrendReportType getType() {
    return type;
  }
  public void setType(TrendReportType type) {
    this.type = type;
  }
  
  private TrendReportTimeInfo time;
  public TrendReportTimeInfo getTime() {
    return time;
  }
  public void setTime(TrendReportTimeInfo time) {
    this.time = time;
  }
  
  private WindInfo wind;
  public WindInfo getWind() {
    return wind;
  }
  public void setWind(WindInfo wind) {
    this.wind = wind;
  }
  
  private TrendVisibilityInfo visibility = TrendVisibilityInfo.createCAVOK();
  public TrendVisibilityInfo getVisibility() {
    return visibility;
  }
  public void setVisibility(TrendVisibilityInfo visibility) {
    this.visibility = visibility;
  }
  
  private TrendPhenomenaInfo phenomenas = TrendPhenomenaInfo.createNSW();
  public TrendPhenomenaInfo getPhenomenas() {
    return phenomenas;
  }
  public void setPhenomenas(TrendPhenomenaInfo phenomenas) {
    if (phenomenas == null)
      throw new NullArgumentException("[phenomenas]");
    this.phenomenas = phenomenas;
  }
  
  private TrendCloudInfo cloudInfo = TrendCloudInfo.createNSC();
  public TrendCloudInfo getCloudInfo() {
    return cloudInfo;
  }
  public void setCloudInfo(TrendCloudInfo cloudInfo) {
    if (cloudInfo == null)
      throw new NullArgumentException("[cloudInfo]");
    this.cloudInfo = cloudInfo;
  }
  
  
  
  
}
