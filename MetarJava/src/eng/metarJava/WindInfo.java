package eng.metarJava;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Variation;

/**
 * Represents info about the wind.
 *
 * @author Marek Vajgl
 */
public class WindInfo {



  private final Heading direction;
  private final double speedInKmh;
  private final Double gustSpeedInKmh;
  private final Variation<Heading> variation;

  public static WindInfo createCalm() {
    WindInfo ret = new WindInfo(new Heading(0), 0, null, null);
    return ret;
  }
    
  public static WindInfo create(Heading direction, double speedInKmh) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speedInKmh, null, null);
    return ret;
  }

  public static WindInfo create(Heading direction, double speedInKmh, double gustSpeedInKmh) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speedInKmh, gustSpeedInKmh, null);
    return ret;
  }

  public static WindInfo create(Heading direction, double speedInKmh, Variation<Heading> variation) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    if (variation == null) {
      throw new NullArgumentException("[variation]");
    }
    WindInfo ret = createWithOptionals(direction, speedInKmh, null, variation);
    return ret;
  }

  public static WindInfo create(Heading direction, double speedInKmh, double gustSpeedInKmh, Variation<Heading> variation) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    if (variation == null) {
      throw new NullArgumentException("[variation]");
    }
    WindInfo ret = createWithOptionals(direction, speedInKmh, gustSpeedInKmh, variation);
    return ret;
  }

  public static WindInfo createVRB(double speedInKmh) {
    WindInfo ret = createWithOptionals(null, speedInKmh, null, null);
    return ret;
  }

  public static WindInfo createVRB(double speedInKmh, double gustSpeedInKmh) {
    WindInfo ret = createWithOptionals(null, speedInKmh, gustSpeedInKmh, null);
    return ret;
  }

  public static WindInfo createWithOptionals(Heading directionOrNull, double speedInKmh, Double gustSpeedInKmhOrNull, Variation<Heading> variationOrNull) {
    WindInfo ret = new WindInfo(directionOrNull, speedInKmh, gustSpeedInKmhOrNull, variationOrNull);
    return ret;
  }

  protected WindInfo(Heading direction, double speedInKmh, Double gustSpeedInKmh,
          Variation<Heading> variation) {
    if (speedInKmh < 0) {
      throw new IllegalArgumentException("[speedInKmh] cannot be negative.");
    }
    if (gustSpeedInKmh != null && gustSpeedInKmh < 0) {
      throw new IllegalArgumentException("[gustSpeedInKmh] (if present) cannot be negative.");
    }
    if (gustSpeedInKmh != null && speedInKmh >= gustSpeedInKmh) {
      throw new IllegalArgumentException("[gustSpeedInKmh], if present, must be greater than [speedInKmh]");
    }
    if (direction == null && variation != null) {
      throw new IllegalArgumentException("[variation] cannot be set if [direction] is null.");
    }

    this.direction = direction;
    this.speedInKmh = speedInKmh;
    this.gustSpeedInKmh = gustSpeedInKmh;
    this.variation = variation;
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

  /**
   * Returns true if wind is variating between different headings.
   *
   * @return Wind is variating if heading variations are set.
   */
  public boolean isVariating() {
    return this.variation != null;
  }

  public Variation<Heading> getVariation() {
    return variation;
  }

  public Double getGustSpeedInKmh() {
    return gustSpeedInKmh;
  }

}
