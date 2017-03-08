/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public class DayHourMinute {

  private final int day;
  private final int hour;
  private final int minute;

  public DayHourMinute(int day, int hour, int minute) {
    if (Shared.isBetween(day, 1, 32) == false) {
      throw new IllegalArgumentException("[Day] value must be between 1 and 31. Value is " + day + ".");
    }
    if (Shared.isBetween(hour, 0, 24) == false) {
      throw new IllegalArgumentException("[Hour] value must be between 0 and 23. Value is " + hour + ".");
    }
    if (Shared.isBetween(minute, 0, 60) == false) {
      throw new IllegalArgumentException("[Minute] value must be between 0 and 60. Value is " + minute + ".");
    }
    this.day = day;
    this.hour = hour;
    this.minute = minute;
  }

  public int getDay() {
    return day;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  @Override
  public String toString() {
    String ret = String.format("%02d%02d%02dZ", this.day, this.hour, this.minute);
    return ret;
  }
}
