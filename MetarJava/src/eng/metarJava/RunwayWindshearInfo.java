package eng.metarJava;

import eng.metarJava.exception.NullArgumentException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Marek Vajgl
 */
public class RunwayWindshearInfo {

  private final Set<String> runwayDesignators;
  private final boolean allRunways;

  public RunwayWindshearInfo(boolean isWindshearAllRunways) {
    if (!isWindshearAllRunways){
      throw new IllegalArgumentException("This constructor may be called only with [isWindshearAllRunways] as true. Use different constructor otherwise.");
    }
    this.allRunways = true;
    this.runwayDesignators = null;
  }

  public RunwayWindshearInfo(Set<String> runwayDesignators) {
    if (runwayDesignators == null)
      throw new NullArgumentException("[runwayDesignators]");
    this.runwayDesignators = runwayDesignators;
    this.allRunways = false;
  }

  public boolean isAllRunways() {
    return allRunways;
  }
  
  /**
   * Returns true if list of designators is empty.
   * @return 
   */
  public boolean isEmpty (){
    return this.runwayDesignators.isEmpty();
  }

  public Set<String> getRunwayDesignators() {
    return runwayDesignators;
  }

  
}
