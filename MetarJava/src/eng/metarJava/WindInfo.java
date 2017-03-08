package eng.metarJava;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.Heading;

/**
 * Represents info about the wind.
 *
 * @author Marek Vajgl
 */
public class WindInfo {

  private final Heading direction;
  private final double speedInKmh;
  private final Double gustSpeedInKmh;

  public WindInfo(Heading direction, double speedInKmh, Double gustSpeedInKmh) {
    if (speedInKmh < 0) {
      throw new IllegalArgumentException("speedInKmh cannot be negative.");
    }
    if (gustSpeedInKmh != null && gustSpeedInKmh < 0) {
      throw new IllegalArgumentException("gustSpeedInKmh (if used) cannot be negative.");
    }
    this.direction = direction;
    this.speedInKmh = speedInKmh;
    this.gustSpeedInKmh = gustSpeedInKmh;
  }

  public WindInfo(Heading direction, int speed, Integer gustSpeed, SpeedUnit speedUnit) {
    this(direction,
            SpeedUnit.convert(speed, speedUnit, SpeedUnit.KMH),
            SpeedUnit.convert(gustSpeed, speedUnit, SpeedUnit.KMH));
  }

  /**
   * Returns direcion of wind, null if wind is variable.
   *
   * @return Direction (heading) or null
   */
  public Heading getDirection() {
    return direction;
  }

  public double getSpeedInKmh() {
    return speedInKmh;
  }

  /**
   * Returns speed in specified unit rounded to integer.
   *
   * @param unit
   * @return
   */
  public int getSpeed(SpeedUnit unit) {
    double tmp = SpeedUnit.convert(this.speedInKmh, SpeedUnit.KMH, unit);
    int ret = (int) Math.round(tmp);
    return ret;
  }

  /**
   * Returns gusting speed or null if there are no gusts.
   *
   * @param unit
   * @return Gusting speed or null.
   */
  public Integer tryGetGustingSpeed(SpeedUnit unit) {
    Double tmp = SpeedUnit.convert(this.gustSpeedInKmh, SpeedUnit.KMH, unit);
    Integer ret;
    if (tmp == null) {
      ret = null;
    } else {
      ret = (int) Math.round((double) tmp);
    }
    return ret;
  }

  /**
   * Return gusting speed or exception if there are no gusts.
   *
   * @param unit
   * @return Gusting speed as double. UnsupportedOperationException if there are no gusts.
   */
  public int getGustingSpeed(SpeedUnit unit) {
    if (this.isGusting() == false) {
      throw new UnsupportedOperationException("Cannot get gusting speed for no gusts. Try [tryGetGustingSpeed()] instead?");
    }
    return (int) tryGetGustingSpeed(unit);
  }

  /**
   * Returns true if there is gusting wind.
   *
   * @return
   */
  public boolean isGusting() {
    return this.gustSpeedInKmh != null;
  }

  /**
   * Retuns true if wind is variable.
   *
   * @return Wind is variable if no heading is set.
   */
  public boolean isVariable() {
    return this.direction == null;
  }

  public Double getGustSpeedInKmh() {
    return gustSpeedInKmh;
  }

}
