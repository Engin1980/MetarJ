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
  
  private String icao;
  public String getIcao() {
    return icao;
  }
  public void setIcao(String icao) {
    if (icao == null) throw new NullArgumentException("icao");
    if (icao.length() != 4) throw new IllegalArgumentException("Icao length must be 4 characters.");
    if (icao.equals("////")) throw new IllegalArgumentException("ICAO cannot be set to ////");
    this.icao = icao;
  }
  
  private DayHourMinute time;
  public DayHourMinute getTime() {
    return time;
  }
  public void setTime(DayHourMinute time) {
    this.time = time;
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
