package eng.metarJava;

import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.ReadOnlyList;
import java.util.List;

/**
 * Represents wind-shear warning info about one runway.
 * @author Marek Vajgl
 */
public class RunwayWindshearInfo {

  private final ReadOnlyList<String> runwayDesignators;
  private final boolean allRunways;

  public static RunwayWindshearInfo create(List<String> runwayDesignators) {
    RunwayWindshearInfo ret = create(new ReadOnlyList(runwayDesignators));
    return ret;
  }
  
  public static RunwayWindshearInfo create(ReadOnlyList<String> runwayDesignators) {
    RunwayWindshearInfo ret = new RunwayWindshearInfo(runwayDesignators, false);
    return ret;
  }

  public static RunwayWindshearInfo createAllRWY() {
    RunwayWindshearInfo ret = new RunwayWindshearInfo(null, true);
    return ret;
  }

  protected RunwayWindshearInfo(ReadOnlyList<String> runwayDesignators, boolean isWindshearAllRunways) {
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

  public ReadOnlyList<String> getRunwayDesignators() {
    return runwayDesignators;
  }

}
