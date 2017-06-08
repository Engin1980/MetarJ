package eng.metarJava;

import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.support.Variation;

/**
 * Represens visual range of one runway.
 * @author Marek Vajgl
 */
public class RunwayVisualRange {

  private final String runwayDesignator;
  private final Integer visibilityInMeters;
  private final Variation<Integer> variatingVisibilityInMeters;

  public static RunwayVisualRange create(String runwayDesignator, int visibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, visibilityInMeters);
    return ret;
  }

  public static RunwayVisualRange create(String runwayDesignator, Variation<Integer> variatingVisibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, variatingVisibilityInMeters);
    return ret;
  }

  protected RunwayVisualRange(String runwayDesignator, Integer visibilityInMeters) {
    if (visibilityInMeters == null)
      throw new IllegalArgumentException("[visibilityInMeters] must be not null.");
    if (visibilityInMeters < 0)
      throw new IllegalArgumentException("[visibilityInMeters] must be positive (currently " + visibilityInMeters + ").");
    
    this.runwayDesignator = runwayDesignator;
    this.visibilityInMeters = visibilityInMeters;
    this.variatingVisibilityInMeters = null;
  }

  protected RunwayVisualRange(String runwayDesignator, Variation<Integer> variatingVisibilityInMeters) {
    if (variatingVisibilityInMeters == null)
      throw new IllegalArgumentException("[variatingVisibilityInMeters] must be not null.");
    if (variatingVisibilityInMeters.getFrom() < 0 
            || variatingVisibilityInMeters.getTo() < 0 
            || variatingVisibilityInMeters.getFrom() >= variatingVisibilityInMeters.getTo() )
      throw new IllegalArgumentException("[variatingVisibilityInMeters] must be increasing positive (currently " + variatingVisibilityInMeters.toString() + ").");
    
    this.runwayDesignator = runwayDesignator;
    this.variatingVisibilityInMeters = variatingVisibilityInMeters;
    this.visibilityInMeters = null;
  }

  public String getRunwayDesignator() {
    return runwayDesignator;
  }

  public int getVisibilityInMeters() {
    if (this.visibilityInMeters == null)
      throw new NonsenseRequestException(
              "Cannot obtain visibility in meters when visibility is variating. Check isVariating() function or call getVariatingVisibilityInMeters.");
    return visibilityInMeters;
  }

  public Variation<Integer> getVariatingVisibilityInMeters() {
//    if (this.variatingVisibilityInMeters == null)
//      throw new NonsenseRequestException(
//        "Cannot obtaing variating visibility if only fixed visibility is set. Check isVariating() function or call getVisibilityInMeters().");
    return variatingVisibilityInMeters;
  }

  public boolean isVariating() {
    return this.variatingVisibilityInMeters != null;
  }

}
