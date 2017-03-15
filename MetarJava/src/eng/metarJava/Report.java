/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava;

import eng.metarJava.enums.*;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.DayHourMinute;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents METAR/SPECI report.
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
    if (icao == null) {
      throw new NullArgumentException("icao");
    }
    if (icao.length() != 4) {
      throw new IllegalArgumentException("Icao length must be 4 characters.");
    }
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

  private VisibilityInfo visibility;
  public VisibilityInfo getVisibility() {
    return visibility;
  }
  public void setVisibility(VisibilityInfo visibility) {
    this.visibility = visibility;
  }

  private final List<RunwayVisualRange> runwayVisualRanges = new ArrayList<>();
  public List<RunwayVisualRange> getRunwayVisualRanges() {
    return runwayVisualRanges;
  }

  private final List<PhenomenaInfo> phenomenas = new ArrayList<>();
  public List<PhenomenaInfo> getPhenomenas() {
    return phenomenas;
  }

  private CloudInfo clouds;
  public CloudInfo getClouds() {
    return clouds;
  }
  public void setClouds(CloudInfo clouds) {
    this.clouds = clouds;
  }

  private int temperature;
  public int getTemperature() {
    return temperature;
  }
  public void setTemperature(int temperature) {
    this.temperature = temperature;
  }

  private int dewPoint;
  public int getDewPoint() {
    return dewPoint;
  }
  public void setDewPoint(int dewPoint) {
    this.dewPoint = dewPoint;
  }
  
  private int pressureInHpa;
  public int getPressureInHpa() {
    return pressureInHpa;
  }
  public void setPressureInHpa(int pressureInHpa) {
    this.pressureInHpa = pressureInHpa;
  }
  
  private final List<PhenomenaInfo> recentPhenomenas = new ArrayList<>();
  public List<PhenomenaInfo> getRecentPhenomenas() {
    return recentPhenomenas;
  }
  
  private RunwayWindshearInfo runwayWindshears;
  public RunwayWindshearInfo getRunwayWindshears() {
    return runwayWindshears;
  }
  public void setRunwayWindshears(RunwayWindshearInfo runwayWindshear) {
    this.runwayWindshears = runwayWindshear;
  }
  
  private RunwayStatesInfo runwayStateInfo;
  public RunwayStatesInfo getRunwayStateInfo() {
    return runwayStateInfo;
  }
  public void setRunwayStateInfo(RunwayStatesInfo runwayStateInfo) {
    this.runwayStateInfo = runwayStateInfo;
  }
  
  private TrendInfo trendInfo;
  public TrendInfo getTrendInfo() {
    return trendInfo;
  }
  public void setTrendInfo(TrendInfo trendInfo) {
    this.trendInfo = trendInfo;
  }
  
  private String remark;
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  
}
