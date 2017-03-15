package eng.metarJava;

import eng.metarJava.support.Variation;

/**
 * Represens visual range of one runway.
 * @author Marek Vajgl
 */
public class RunwayVisualRange {

  private final String runwayDesignator;
  private final Integer visibilityInMeters;
  private final Variation<Integer> variatingVisibilityInMeters;

  public static RunwayVisualRange create(String runwayDesignator, Integer visibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, visibilityInMeters);
    return ret;
  }

  public static RunwayVisualRange create(String runwayDesignator, Variation<Integer> variatingVisibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, variatingVisibilityInMeters);
    return ret;
  }

  protected RunwayVisualRange(String runwayDesignator, Integer visibilityInMeters) {
    this.runwayDesignator = runwayDesignator;
    this.visibilityInMeters = visibilityInMeters;
    this.variatingVisibilityInMeters = null;
  }

  protected RunwayVisualRange(String runwayDesignator, Variation<Integer> variatingVisibilityInMeters) {
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

  public boolean isVariating() {
    return this.variatingVisibilityInMeters != null;
  }

}
