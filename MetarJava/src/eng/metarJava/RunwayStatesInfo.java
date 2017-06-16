package eng.metarJava;

import eng.metarJava.exceptions.NullArgumentException;
import eng.metarJava.support.ReadOnlyList;
import java.util.List;

/**
 * Represents state of the airport ruwnays.
 * @author Marek Vajgl
 */
public class RunwayStatesInfo {
  private final boolean snowClosed;
  private final ReadOnlyList<RunwayState> runwayStates;
  
  public static RunwayStatesInfo createSNOCLO(){
    RunwayStatesInfo ret = new RunwayStatesInfo(true, null);
    return ret;
  }
  
  public static RunwayStatesInfo create(List<RunwayState> runwayStates){
    RunwayStatesInfo ret = create (new ReadOnlyList(runwayStates));
    return ret;
  }
  
  public static RunwayStatesInfo create(ReadOnlyList<RunwayState> runwayStates){
    RunwayStatesInfo ret = new RunwayStatesInfo(false, new ReadOnlyList(runwayStates));
    return ret;
  }

  protected RunwayStatesInfo(boolean snowClosed, ReadOnlyList<RunwayState> runwayStates) {
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

  public ReadOnlyList<RunwayState> getRunwayStates() {
    return runwayStates;
  }
  
  public boolean isEmpty(){
    if (snowClosed) return false;
    return this.runwayStates == null || this.runwayStates.isEmpty();
  }
}
