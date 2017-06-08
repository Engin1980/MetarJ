package eng.metarJava;

import eng.metarJava.enums.SpeedUnit;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.Heading;
import eng.metarJava.support.Speed;
import eng.metarJava.support.Variation;

/**
 * Represents info about the wind. Wind can have up to four properties. - {@link #getDirection() Direction} (of type
 * {@linkplain Heading}) determines heading of the wind. If wind is variable (VRB), heading is null. - {@link #getSpeed() Wind speed}
 * is represented by {@linkplain Speed}. Cannot be null. - {@link #getSpeed() Wind gusting speed} is represented by {@linkplain Speed}.
 * If null, means, that there are no gusts. - {@link #getVariation()  Direciton variation} of type {@linkplain Variation} represens
 * heading oscilations. If null, there are no variations.
 * @author Marek Vajgl
 */
public class WindInfo {

  private final Heading direction;
  private final Speed speed;
  private final Speed gustSpeed;
  private final Variation<Heading> variation;

  /**
   * Creates calm wind.
   *
   * @return Instance of calm wind. Calm wind has {@link #getDirection() direction} set to zero and {@link #getSpeed() wind speed} set
   * to zero. It has no gusts and no variations.
   */
  public static WindInfo createCalm() {
    WindInfo ret = new WindInfo(new Heading(0), new Speed(0, SpeedUnit.KMH), null, null);
    return ret;
  }

  /**
   * Creates wind with {@link #getDirection() direction} and {@link #getSpeed() speed}.
   *
   * @param direction {@link Heading Heading} instance, mandatory.
   * @param speed {@link Speed Speed} instance, not null, positive.
   * @return Instance of wind. Use this method when you have direction and speed, but no gusts and no variation. When direction and
   * speed is zero, {@link #createCalm() calm wind} is created.
   */
  public static WindInfo create(Heading direction, Speed speed) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speed, null, null);
    return ret;
  }

  /**
   * Creates wind with {@link #getDirection() direction} and {@link #getSpeed() speed}.
   *
   * @param direction {@link Heading Heading} instance, mandatory.
   * @param speed {@link Speed Speed} instance, not null, positive.
   * @param gustSpeed {@link Speed gust speed} instance, if used, positive.
   * @return Instance of wind. Use this method when you have direction and speed, but no variation. If gust-speed is null, there are
   * expected to be no gusts, however, when gust-speed is not-null, its speed must be greater than speed value.
   */
  public static WindInfo create(Heading direction, Speed speed, Speed gustSpeed) {
    if (direction == null) {
      throw new NullArgumentException("[direction]");
    }
    WindInfo ret = createWithOptionals(direction, speed, gustSpeed, null);
    return ret;
  }

  /**
   * Creates wind with {@link #getDirection() direction}, {@link #getSpeed() speed}, and {@link #getVariation() variating direction}.
   *
   * @param direction {@link Heading Heading} instance, mandatory.
   * @param speed {@link Speed Speed} instance, not null, positive.
   * @param variation {@link Variation Variation[Heading]} instance, not null.
   * @return Instance of wind. Use this method when you have direction and speed and direction variation, but no gusts.
   */
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

  /**
   * Creates wind with {@link #getDirection() direction}, {@link #getSpeed() speed},
   * {@link #getGustingSpeed() gusting speed} and {@link #getVariation() variating direction}.
   *
   * @param direction {@link Heading Heading} instance, mandatory.
   * @param speed {@link Speed Speed} instance, not null, positive.
   * @param gustSpeed {@link Speed gust speed} instance, if used, positive.
   * @param variation {@link Variation Variation[Heading]} instance, not null.
   * @return Instance of wind. Use this method when you have direction and speed, gusts and direction variation.
   */
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

  /**
   * Creates wind with variable {@link #getDirection() heading} and {@link #getSpeed() speed}.
   *
   * @param speed
   * @return Use this method when wind direction is variable - VRB.
   * @see #isVariable()
   */
  public static WindInfo createVRB(Speed speed) {
    WindInfo ret = createWithOptionals(null, speed, null, null);
    return ret;
  }

  /**
   * Creates wind with variable {@link #getDirection() heading}, {@link #getSpeed() speed} and
   * {@link #getGustingSpeed() gusting speed}.
   *
   * @param speed
   * @return Use this method when wind direction is variable - VRB.
   * @see #isVariable()
   */
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

  /**
   * Returns wind speed.
   *
   * @return
   */
  public Speed getSpeed() {
    return speed;
  }

  /**
   * Return gusting speed or exception if there are no gusts.
   *
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
   * @return Wind is variable if no heading is set. Wind is variable when there is no determination about wind direction. VRB
   * abbreviation is used in METAR report. Do not confuse with {@linkplain  #isVariating()}.
   * @see #isVariable()
   */
  public boolean isVariable() {
    return this.direction == null;
  }

  /**
   * Returns true if wind is variating between headings.
   *
   * @return Wind is variating if heading variations are set. Wind is variable when there is no determination about wind direction. VRB
   * abbreviation is used in METAR report. Do not confuse with {@linkplain  #isVariating()}. Wind is variating when its direction is
   * oscilating between two headings. xxxVxxx is used in METAR. Do not confuse with {@linkplain  #isVariable()}.
   * @see #isVariating()
   */
  public boolean isVariating() {
    return this.variation != null;
  }

  /**
   * Returns variations of the heading, or NULL if there are no variations.
   *
   * @return Value or null.
   * @see #isVariating()
   */
  public Variation<Heading> getVariation() {
    return variation;
  }
}
