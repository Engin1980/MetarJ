package eng.metarJava;

import eng.metarJava.exception.NullArgumentException;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class RunwayStatesInfo {
  private final boolean snowClosed;
  private final List<RunwayState> runwayStates;

  public RunwayStatesInfo(boolean isSnowClosed) {
    if (!isSnowClosed){
      throw new IllegalArgumentException("This constructor may be called only with [isSnowClosed] as true. Specify runway data otherwise (use different constructor).");
    }
    this.snowClosed = true;
    this.runwayStates = null;
  }

  public RunwayStatesInfo(List<RunwayState> runwayStates) {
    if (runwayStates == null)
      throw new NullArgumentException("[runwayStates]");
    this.snowClosed = false;
    this.runwayStates = runwayStates;
  }

  public boolean isSnowClosed() {
    return snowClosed;
  }

  public List<RunwayState> getRunwayStates() {
    return runwayStates;
  }
  
  public boolean isEmpty(){
    return this.runwayStates.isEmpty();
  }
}
