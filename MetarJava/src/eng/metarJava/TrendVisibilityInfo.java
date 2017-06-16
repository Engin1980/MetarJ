package eng.metarJava;

import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.exceptions.NonsenseRequestException;

/**
 *
 * @author Marek Vajgl
 */
public class TrendVisibilityInfo {

  private final double visibilityInMeters;
  private final boolean CAVOK;

  public static TrendVisibilityInfo createCAVOK() {
    TrendVisibilityInfo ret = new TrendVisibilityInfo(0, true);
    return ret;
  }

  public static TrendVisibilityInfo create(double visibilityInMeters) {
    TrendVisibilityInfo ret = new TrendVisibilityInfo(visibilityInMeters, false);
    return ret;
  }

  protected TrendVisibilityInfo(double visibilityInMeters, boolean isCAVOK) {
    if (isCAVOK) {
      this.visibilityInMeters = Integer.MAX_VALUE;
      this.CAVOK = true;
    } else {
      if (visibilityInMeters < 0) {
        throw new IllegalArgumentException("[visibilityInMeters], if not null, must be zero or positive.");
      }
      this.visibilityInMeters = visibilityInMeters;
      this.CAVOK = false;
    }
  }

  /**
   * Gets visibility in meters.
   *
   * @return
   */
  public double getVisibilityInMeters() {
    if (CAVOK) {
      throw new NonsenseRequestException("Unable to get visibility distance for CAVOK visibility.");
    }
    return visibilityInMeters;
  }
  
  /**
   * Gets visibility in a specified unit.
   * @param unit unit in which value should be returned
   * @return value converted to specified unit
   */
  public double getVisibility(DistanceUnit unit){
    double ret = DistanceUnit.convert(this.visibilityInMeters, DistanceUnit.meters, unit);
    return ret;
  }

  /**
   * Return if visibility is CAVOK.
   *
   * @return Visibility is CaVOk if visibilityInMeters is not set.
   */
  public boolean isCAVOK() {
    return this.CAVOK;
  }
}
