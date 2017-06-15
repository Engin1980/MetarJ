package eng.metarJava;

import eng.metarJava.enums.DistanceUnit;

/**
 * Represents visibility
 *
 * @author Marek Vajgl
 */
public class VisibilityInfo extends TrendVisibilityInfo {

  private final boolean noDirectionalVisibility;
  private final VisibilityVariability variability;

  /**
   * Creates new visibility info by distance in meters.
   * @param visibilityInMeters
   * @return 
   */
  public static VisibilityInfo create(double visibilityInMeters){
    VisibilityInfo ret = new VisibilityInfo(visibilityInMeters, false, false, null);
    return ret;
  }
  
  /**
   * Creates new visibility info by distance in meters.
   * @param visibility visibility in unit specified by second parameter
   * @param unit distance unit of visibility
   * @return 
   */
  public static VisibilityInfo create(double visibility, DistanceUnit unit){
    double visInM = DistanceUnit.convert(visibility, unit, DistanceUnit.meters);
    VisibilityInfo ret = new VisibilityInfo(visInM, false, false, null);
    return ret;
  }
  
  /**
   * Creates new visibility info by distance and distance variability.
   * @param visibilityInMeters
   * @param variability
   * @return 
   */
  public static VisibilityInfo create(double visibilityInMeters, VisibilityVariability variability){
    VisibilityInfo ret = new VisibilityInfo(visibilityInMeters, false, false, variability);
    return ret;
  }
  
  /**
   * Creates new visibility with NDV - no-direction-visibility flag set on.
   * @param visibilityInMeters
   * @return 
   */
  public static VisibilityInfo createWithNDV(double visibilityInMeters){
    VisibilityInfo ret = new VisibilityInfo(visibilityInMeters, false, true, null);
    return ret;
  }
  
  /**
   * Creates CAVOK visiblity.
   * @return 
   */
  public static VisibilityInfo createCAVOK(){
    VisibilityInfo ret = new VisibilityInfo(0, true, false, null);
    return ret;
  }

  protected VisibilityInfo(
          double visibilityInMeters, boolean isCAVOK,
          boolean noDirectionalVisibility, 
          VisibilityVariability variability) {
    super(visibilityInMeters, isCAVOK);
    if (isCAVOK){
      this.noDirectionalVisibility = false;
      this.variability = null;
    } else {
      this.noDirectionalVisibility = noDirectionalVisibility;
      this.variability = variability;
    }
  }
  
  /**
   * Return if NDV flag is set.
   *
   * @return True or false if NDV is set.
   */
  public boolean isNoDirectionalVisibility() {
    return this.noDirectionalVisibility;
  }

  /**
   * Return true if visibility is variating.
   *
   * @return
   */
  public boolean isVariating() {
    return this.variability != null;
  }

  /**
   * Return variability if visibility is variating, or null.
   *
   * @return
   */
  public VisibilityVariability getVariability() {
    return this.variability;
  }
}
