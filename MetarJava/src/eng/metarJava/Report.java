/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava;

import eng.metarJava.enums.*;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.DayHourMinute;

/**
 *
 * @author Marek Vajgl
 */
public class Report {
  private ReportType type;
  public ReportType getType() {
    return type;
  }
  public void setType(ReportType type) {
    this.type = type;
  }
  
  /**
   * COR
   */
  private boolean correction;
  public boolean isCorrection() {
    return correction;
  }
  public void setCorrection(boolean correction) {
    this.correction = correction;
  }
  
  private String icao = "////";
  public String getIcao() {
    return icao;
  }
  public void setIcao(String icao) {
    if (icao == null) throw new NullArgumentException("icao");
    if (icao.length() != 4) throw new IllegalArgumentException("Icao length must be 4 characters.");
    this.icao = icao;
  }
  
  private DayHourMinute dayTime;
  public DayHourMinute getDayTime() {
    return dayTime;
  }
  public void setDayTime(DayHourMinute time) {
    this.dayTime = time;
  }
  
  /**
   * NIL
   */
  private boolean nil;
  public boolean isNil() {
    return nil;
  }
  public void setNil(boolean nil) {
    this.nil = nil;
  }
  
  /**
   * AUTO
   */
  private boolean auto;
  public boolean isAuto() {
    return auto;
  }
  public void setAuto(boolean auto) {
    this.auto = auto;
  }
  
  private WindInfo wind;
  public WindInfo getWind() {
    return wind;
  }
  public void setWind(WindInfo wind) {
    this.wind = wind;
  }
  
}
