package eng.metarJava;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.Heading;

/**
 * Represents info about the wind.
 * @author Marek Vajgl
 */
public class WindInfo {
  private final Heading direction;
  private final double speedInKmh;
  private final Double gustSpeedInKmh;

  public WindInfo(Heading direction, double speedInKmh, Double gustSpeedInKmh) {
    if (direction == null) throw new NullArgumentException("direction");
    if (speedInKmh < 0) throw new IllegalArgumentException("speedInKmh cannot be negative.");
    if (gustSpeedInKmh != null && gustSpeedInKmh < 0) throw new IllegalArgumentException("gustSpeedInKmh (if used) cannot be negative.");
    this.direction = direction;
    this.speedInKmh = speedInKmh;
    this.gustSpeedInKmh = gustSpeedInKmh;
  }
  
  public WindInfo(Heading direction, int speed, Integer gustSpeed, SpeedUnit speedUnit) {
    this( direction,
            (double) SpeedUnit.convert(speed, speedUnit, SpeedUnit.KMH),
            (Double) SpeedUnit.convert(gustSpeed, speedUnit, SpeedUnit.KMH));
  }

  public Heading getDirection() {
    return direction;
  }

  public double getSpeedInKmh() {
    return speedInKmh;
  }
  
  public double getSpeed(SpeedUnit unit){
    return SpeedUnit.convert(this.speedInKmh, SpeedUnit.KMH, unit);
  }
  
  /**
   * Returns gusting speed or null if there are no gusts.
   * @param unit
   * @return Gusting speed or null.
   */
  public Double tryGetGustingSpeed(SpeedUnit unit){
    return SpeedUnit.convert(this.gustSpeedInKmh, SpeedUnit.KMH, unit);
  }
  
  /**
   * Return gusting speed or exception if there are no gusts.
   * @param unit
   * @return Gusting speed as double. UnsupportedOperationException if there are no gusts.
   */
  public Double getGustingSpeed(SpeedUnit unit){
    if (this.isGusting() == false)
      throw new UnsupportedOperationException("Cannot get gusting speed for no gusts. Try [tryGetGustingSpeed()] instead?");
    return (double) tryGetGustingSpeed(unit);
  }

  /**
   * Returns true if there is gusting wind.
   * @return 
   */
  public boolean isGusting(){
    return this.gustSpeedInKmh != null;
  }
  
  public Double getGustSpeedInKmh() {
    return gustSpeedInKmh;
  }
  
}
