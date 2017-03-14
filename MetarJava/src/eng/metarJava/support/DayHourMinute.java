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
public class DayHourMinute extends HourMinute {

  private final int day;

  public DayHourMinute(int day, int hour, int minute) {
    super(hour, minute);
    if (Shared.isBetween(day, 1, 32) == false) {
      throw new IllegalArgumentException("[Day] value must be between 1 and 31. Value is " + day + ".");
    }

    this.day = day;
  }

  public int getDay() {
    return day;
  }

  @Override
  public String toString() {
    String ret = String.format("%02d%02d%02dZ", this.day, this.getHour(), this.getMinute());
    return ret;
  }
}
