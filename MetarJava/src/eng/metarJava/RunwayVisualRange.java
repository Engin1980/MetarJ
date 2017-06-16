package eng.metarJava;

import eng.metarJava.enums.DistanceUnit;
import eng.metarJava.exceptions.NonsenseRequestException;
import eng.metarJava.support.Variation;

/**
 * Represens visual range of one runway.
 *
 * @author Marek Vajgl
 */
public class RunwayVisualRange {

  private final String runwayDesignator;
  private final Double visibilityInMeters;
  private final Variation<Double> variatingVisibilityInMeters;

  public static RunwayVisualRange create(String runwayDesignator, double visibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, visibilityInMeters);
    return ret;
  }

  public static RunwayVisualRange create(String runwayDesignator, double visibility, DistanceUnit unit) {
    double visInM = DistanceUnit.convert(visibility, unit, DistanceUnit.meters);
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, visInM);
    return ret;
  }

  public static RunwayVisualRange create(String runwayDesignator, Variation<Double> variatingVisibility, DistanceUnit unit) {
    Variation<Double> varInM = new Variation<>(
            DistanceUnit.convert(variatingVisibility.getFrom(), unit, DistanceUnit.meters),
            DistanceUnit.convert(variatingVisibility.getTo(), unit, DistanceUnit.meters));
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, varInM);
    return ret;
  }

  public static RunwayVisualRange create(String runwayDesignator, Variation<Double> variatingVisibilityInMeters) {
    RunwayVisualRange ret = new RunwayVisualRange(runwayDesignator, variatingVisibilityInMeters);
    return ret;
  }

  protected RunwayVisualRange(String runwayDesignator, Double visibilityInMeters) {
    if (visibilityInMeters == null) {
      throw new IllegalArgumentException("[visibilityInMeters] must be not null.");
    }
    if (visibilityInMeters < 0) {
      throw new IllegalArgumentException("[visibilityInMeters] must be positive (currently " + visibilityInMeters + ").");
    }

    this.runwayDesignator = runwayDesignator;
    this.visibilityInMeters = visibilityInMeters;
    this.variatingVisibilityInMeters = null;
  }

  protected RunwayVisualRange(String runwayDesignator, Variation<Double> variatingVisibilityInMeters) {
    if (variatingVisibilityInMeters == null) {
      throw new IllegalArgumentException("[variatingVisibilityInMeters] must be not null.");
    }
    if (variatingVisibilityInMeters.getFrom() < 0
            || variatingVisibilityInMeters.getTo() < 0
            || variatingVisibilityInMeters.getFrom() >= variatingVisibilityInMeters.getTo()) {
      throw new IllegalArgumentException("[variatingVisibilityInMeters] must be increasing positive (currently " + variatingVisibilityInMeters.toString() + ").");
    }

    this.runwayDesignator = runwayDesignator;
    this.variatingVisibilityInMeters = variatingVisibilityInMeters;
    this.visibilityInMeters = null;
  }

  public String getRunwayDesignator() {
    return runwayDesignator;
  }

  public Double getVisibilityInMeters() {
    if (this.visibilityInMeters == null) {
      throw new NonsenseRequestException(
              "Cannot obtain visibility in meters when visibility is variating. Check isVariating() function or call getVariatingVisibilityInMeters.");
    }
    return visibilityInMeters;
  }

  public Double getVisibility(DistanceUnit unit) {
    Double inM = this.getVisibilityInMeters();
    Double ret = DistanceUnit.convert(inM, DistanceUnit.meters, unit);
    return ret;
  }

  public Variation<Double> getVariatingVisibilityInMeters() {
    if (this.variatingVisibilityInMeters == null) {
      throw new NonsenseRequestException(
              "Cannot obtaing variating visibility if only fixed visibility is set. Check isVariating() function or call getVisibilityInMeters().");
    }
    return variatingVisibilityInMeters;
  }

  public Variation<Double> getVariatingVisibility(DistanceUnit unit) {
    Variation<Double> tmp = getVariatingVisibilityInMeters();
    Variation<Double> ret = new Variation<>(
            DistanceUnit.convert(tmp.getFrom(), DistanceUnit.meters, unit),
            DistanceUnit.convert(tmp.getTo(), DistanceUnit.meters, unit)
    );
    return ret;
  }

  public boolean isVariating() {
    return this.variatingVisibilityInMeters != null;
  }

}
