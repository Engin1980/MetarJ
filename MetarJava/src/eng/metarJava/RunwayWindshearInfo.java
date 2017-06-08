package eng.metarJava;

import eng.metarJava.exception.NullArgumentException;
import java.util.List;
import java.util.Set;

/**
 * Represents wind-shear warning info about one runway.
 * @author Marek Vajgl
 */
public class RunwayWindshearInfo {

  private final Set<String> runwayDesignators;
  private final boolean allRunways;

  public static RunwayWindshearInfo create(Set<String> runwayDesignators) {
    RunwayWindshearInfo ret = new RunwayWindshearInfo(runwayDesignators, false);
    return ret;
  }

  public static RunwayWindshearInfo createAllRWY() {
    RunwayWindshearInfo ret = new RunwayWindshearInfo(null, true);
    return ret;
  }

  protected RunwayWindshearInfo(Set<String> runwayDesignators, boolean isWindshearAllRunways) {
    if (isWindshearAllRunways) {
      this.allRunways = true;
      this.runwayDesignators = null;
    } else {
      if (runwayDesignators == null) {
        throw new NullArgumentException("[runwayDesignators]");
      }
      this.runwayDesignators = runwayDesignators;
      this.allRunways = false;
    }
  }

  public boolean isAllRunways() {
    return allRunways;
  }

  /**
   * Returns true if list of designators is empty.
   *
   * @return
   */
  public boolean isEmpty() {
    return (this.runwayDesignators == null || this.runwayDesignators.isEmpty()) && !this.allRunways;
  }

  public Set<String> getRunwayDesignators() {
    return runwayDesignators;
  }

}
