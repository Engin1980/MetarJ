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
  
  public static RunwayStatesInfo createSNOCLO(){
    RunwayStatesInfo ret = new RunwayStatesInfo(true, null);
    return ret;
  }
  
  public static RunwayStatesInfo create(List<RunwayState> runwayStates){
    RunwayStatesInfo ret = new RunwayStatesInfo(false, runwayStates);
    return ret;
  }

  protected RunwayStatesInfo(boolean snowClosed, List<RunwayState> runwayStates) {
    if (snowClosed){
      this.snowClosed = true;
    this.runwayStates = null;
    } else {
      if (runwayStates == null)
      throw new NullArgumentException("[runwayStates]");
    this.snowClosed = false;
    this.runwayStates = runwayStates;
    }
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
