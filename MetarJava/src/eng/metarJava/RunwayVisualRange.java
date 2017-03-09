package eng.metarJava;

import eng.metarJava.support.Variation;

/**
 *
 * @author Marek Vajgl
 */
public class RunwayVisualRange {
  private final String runwayDesignator;
  private final Integer visibilityInMeters;
  private final Variation<Integer> variatingVisibilityInMeters;

  public RunwayVisualRange(String runwayDesignator, Integer visibilityInMeters) {
    this.runwayDesignator = runwayDesignator;
    this.visibilityInMeters = visibilityInMeters;
    this.variatingVisibilityInMeters = null;
  }

  public RunwayVisualRange(String runwayDesignator, Variation<Integer> variatingVisibilityInMeters) {
    this.runwayDesignator = runwayDesignator;
    this.variatingVisibilityInMeters = variatingVisibilityInMeters;
    this.visibilityInMeters = null;
  }

  public String getRunwayDesignator() {
    return runwayDesignator;
  }

  public Integer getVisibilityInMeters() {
    return visibilityInMeters;
  }

  public Variation<Integer> getVariatingVisibilityInMeters() {
    return variatingVisibilityInMeters;
  }
  
  public boolean isVariating(){
    return this.variatingVisibilityInMeters != null;
  }
  
}
