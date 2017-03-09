package eng.metarJava;

/**
 * Represents visibility
 *
 * @author Marek Vajgl
 */
public class VisibilityInfo {

  private final Integer visibilityInMeters;
  private final boolean noDirectionalVisibility;
  private final VisibilityVariability variability;

  public VisibilityInfo(Integer visibilityInMeters, boolean noDirectionalVisibility, VisibilityVariability variability) {
    if (visibilityInMeters != null && visibilityInMeters < 0) {
      throw new IllegalArgumentException("[visibilityInMeters], if not null, must be zero or positive.");
    }
    if (noDirectionalVisibility && variability != null) {
      throw new IllegalArgumentException("If [noDirectionalVisibility] is true, variability cannot be set.");
    }
    this.visibilityInMeters = visibilityInMeters;
    this.noDirectionalVisibility = noDirectionalVisibility;
    this.variability = variability;
  }

  /** 
   * Return if NDV flag is set.
   * @return True or false if NDV is set.
   */
  public boolean isNoDirectionalVisibility(){
    return this.noDirectionalVisibility;
  }
  
  /**
   * Gets visibility in meters.
   * @return 
   */
  public Integer getVisibilityInMeters() {
    return visibilityInMeters;
  }

  /**
   * Return if visibility is CAVOK.
   * @return 
   * Visibility is CaVOk if visibilityInMeters is not set.
   */
  public boolean isCaVOk() {
    return visibilityInMeters == null;
  }
  
  /**
   * Return true if visibility is variating.
   * @return 
   */
  public boolean isVariating(){
    return this.variability != null;
  }
  
  /**
   * Return variability if visibility is variating, or null.
   * @return 
   */
  public VisibilityVariability getVariability(){
    return this.variability;
  } 
}
