package eng.metarJava.support;

/**
 *
 * @author Marek Vajgl
 */
public class HourMinute {

  private final int hour;
  private final int minute;

  public HourMinute(int hour, int minute) {
    if (Shared.isBetween(hour, 0, 24) == false) {
      throw new IllegalArgumentException("[Hour] value must be between 0 and 23. Value is " + hour + ".");
    }
    if (Shared.isBetween(minute, 0, 60) == false) {
      throw new IllegalArgumentException("[Minute] value must be between 0 and 60. Value is " + minute + ".");
    }

    this.hour = hour;
    this.minute = minute;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  @Override
  public String toString() {
    String ret = String.format("%02d%02d", this.hour, this.minute);
    return ret;
  }

}
