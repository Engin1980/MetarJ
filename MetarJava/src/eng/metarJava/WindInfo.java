package eng.metarJava;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Speed;
import eng.metarJava.support.Variation;

/**
 * Represents info about the wind.
 *
 * @author Marek Vajgl
 */
public class WindInfo {



  private final Heading direction;
  private final Speed speed;
  private final Speed gustSpeed;
  private final Variation<Heading> variation;

  public static WindInfo createCalm() {
    WindInfo ret = new WindInfo(new Heading(0), new Speed(0, SpeedUnit.KMH), null, null);
    return ret;
  }
    
  public static WindInfo create(Heading direction, Speed speed) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speed, null, null);
    return ret;
  }

  public static WindInfo create(Heading direction, Speed speed, Speed gustSpeed) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speed, gustSpeed, null);
    return ret;
  }

  public static WindInfo create(Heading direction, Speed speed, Variation<Heading> variation) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    if (variation == null) {
      throw new NullArgumentException("[variation]");
    }
    WindInfo ret = createWithOptionals(direction, speed, null, variation);
    return ret;
  }

  public static WindInfo create(Heading direction, Speed speed, Speed gustSpeed, Variation<Heading> variation) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    if (variation == null) {
      throw new NullArgumentException("[variation]");
    }
    WindInfo ret = createWithOptionals(direction, speed, gustSpeed, variation);
    return ret;
  }

  public static WindInfo createVRB(Speed speed) {
    WindInfo ret = createWithOptionals(null, speed, null, null);
    return ret;
  }

  public static WindInfo createVRB(Speed speed, Speed gustSpeed) {
    WindInfo ret = createWithOptionals(null, speed, gustSpeed, null);
    return ret;
  }

  public static WindInfo createWithOptionals(Heading directionOrNull, Speed speed, Speed gustSpeedOrNull, Variation<Heading> variationOrNull) {
    WindInfo ret = new WindInfo(directionOrNull, speed, gustSpeedOrNull, variationOrNull);
    return ret;
  }

  protected WindInfo(Heading direction, Speed speed, Speed gustSpeed,
          Variation<Heading> variation) {
    if (gustSpeed != null && speed.getValue() >= gustSpeed.getValue()) {
      throw new IllegalArgumentException("[gustSpeedInKmh], if present, must be greater than [speedInKmh]");
    }
    if (direction == null && variation != null) {
      throw new IllegalArgumentException("[variation] cannot be set if [direction] is null.");
    }

    this.direction = direction;
    this.speed = speed;
    this.gustSpeed = gustSpeed;
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

  public Speed getSpeed() {
    return speed;
  }


  /**
   * Return gusting speed or exception if there are no gusts.
   * @return Gusting speed as double. UnsupportedOperationException if there are no gusts.
   */
  public Speed getGustingSpeed() {
    return this.gustSpeed;
  }

  /**
   * Returns true if there is gusting wind.
   *
   * @return
   */
  public boolean isGusting() {
    return this.gustSpeed != null;
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
}
