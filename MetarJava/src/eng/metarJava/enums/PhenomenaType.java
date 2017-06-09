package eng.metarJava.enums;

import eng.metarJava.support.TryResult;

/**
 *
 * @author Marek Vajgl
 */
public enum PhenomenaType {
  none,
  // descriptors:
  /**
   * Shallow
   */
  MI,
  /**
   * Patches
   */
  BC,
  /**
   * Partial
   */
  PR,
  /**
   * Low drifting
   */
  DR,
  /**
   * Blowing
   */
  BL,
  /**
   * Shower(s)
   */
  SH,
  /**
   * Thunderstorm
   */
  TS,
  /**
   * Freezing (supercooled)
   */
  FZ,
  // precipitations
  /**
   * Drizzle
   */
  DZ,
  /**
   * Rain
   */
  RA,
  /**
   * Snow
   */
  SN,
  /**
   * Snow grains
   */
  SG,
  /**
   * Ice crystals
   */
  IC,
  /**
   * Ice pellets
   */
  PL,
  /**
   * Hail
   */
  GR,
  /**
   * Small hail and/or snow pellets
   */
  GS,
  /**
   * Unknown precipitation
   */
  UP,
  // obscurations
  /**
   * Mist
   */
  BR,
  /**
   * Fog
   */
  FG,
  /**
   * Smoke
   */
  FU,
  /**
   * Volcanic ash
   */
  VA,
  /**
   * Widespread dust
   */
  DU,
  /**
   * Sand
   */
  SA,
  /**
   * Haze
   */
  HZ,
  // others
  /**
   * Dust/sand whirlst
   */
  PO,
  /**
   * Squalls
   */
  SQ,
  /**
   * Funnel cloud(s)
   */
  FC,
  /**
   * Sandstorm
   */
  SS,
  /**
   * Duststorm
   */
  DS;

  /**
   * Try to return enum phenomena value from string double-char representation.
   * @param s Two-char phenomena representation, like RA or SN
   * @return Enum value of phenomena
   */
  public static TryResult<PhenomenaType> tryValueOf(String s) {
    TryResult<PhenomenaType> ret;
    try {
      PhenomenaType tmp = PhenomenaType.valueOf(s);
      ret = TryResult.createSuccess(tmp);
    } catch (Exception ex) {
      ret = TryResult.createFail();
    }
    return ret;
  }

  /**
   * Return true if current phenomena is of type precipitation.
   * @return 
   */
  public boolean isPrecipitation() {
    switch (this) {
      case DZ:
      case RA:
      case SN:
      case SG:
      case IC:
      case PL:
      case GR:
      case GS:
      case UP:
        return true;
      default:
        return false;
    }
  }

  /**
   * Return true if current phenomena of type description.
   * @return 
   */
  public boolean isDescription() {
    switch (this) {
      case MI:
      case BR:
      case PR:
      case DR:
      case BL:
      case SH:
      case TS:
      case FZ:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns true if current phenomena is of type obscuration.
   * @return 
   */
  public boolean isObscuration() {
    switch (this) {
      case BR:
      case FG:
      case FU:
      case VA:
      case DU:
      case SA:
      case HZ:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns true if current phenomena is of type other.
   * @return 
   */
  public boolean isOther() {
    switch (this) {
      case PO:
      case SQ:
      case FC:
      case SS:
      case DS:
        return true;
      default:
        return false;
    }
  }
}
