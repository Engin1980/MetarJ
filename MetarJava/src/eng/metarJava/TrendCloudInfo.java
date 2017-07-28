package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.exceptions.NonsenseRequestException;
import eng.metarJava.support.ReadOnlyList;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendCloudInfo {
  private final ReadOnlyList<CloudMass> masses;
  private final Integer verticalVisibilityInHundredFeet;
  private final boolean verticalVisibility;
  private final CloudInfoSpecialStates specialState;

  public static TrendCloudInfo create(List<CloudMass> cloudMasses){
    TrendCloudInfo ret = create (new ReadOnlyList<>(cloudMasses));
    return ret;
  }
  
  public static TrendCloudInfo create(ReadOnlyList<CloudMass> cloudMasses){
    TrendCloudInfo ret = new TrendCloudInfo(cloudMasses, false, null, CloudInfoSpecialStates.none);
    return ret;
  }
  
  public static TrendCloudInfo createWithVV(int verticalVisibilityInHundredFeet){
    TrendCloudInfo ret = new TrendCloudInfo(null, true, verticalVisibilityInHundredFeet, CloudInfoSpecialStates.none);
    return ret;
  }
  
  /**
   * Creates instance for no-significant clouds (that is NSC in EU or CLR in US reports).
   * @return 
   */
  public static TrendCloudInfo createAsNoSignificant(){
    TrendCloudInfo ret = new TrendCloudInfo(null, false, null, CloudInfoSpecialStates.noSignificantClouds);
    return ret;
  }
  

  protected TrendCloudInfo(ReadOnlyList<CloudMass> masses, boolean isVerticalVisibility, Integer verticalVisibilityInHundredFeet, CloudInfoSpecialStates specialState) {
    if (specialState != CloudInfoSpecialStates.none){
      if (masses != null)
        throw new IllegalArgumentException("[masses] must be null if specialState si set.");
      if (isVerticalVisibility)
        throw new IllegalArgumentException("[isVerticalVisibility] must be false if specialState is set.");
    } else if (isVerticalVisibility){
      if (masses != null)
        throw new IllegalArgumentException("[masses] must be null if VV si set.");
      if (specialState != CloudInfoSpecialStates.none)
        throw new IllegalArgumentException("[specialState] must be none if VV si set.");
    } else {
      if (isVerticalVisibility)
        throw new IllegalArgumentException("[isVerticalVisibility] must be false if [masses] is set.");
      if (specialState != CloudInfoSpecialStates.none)
        throw new IllegalArgumentException("[specialState] must be none if [masses] si set.");
    }
    this.masses = masses;
    this.verticalVisibility = isVerticalVisibility;
    this.verticalVisibilityInHundredFeet = verticalVisibilityInHundredFeet;
    this.specialState = specialState;
  }

  public ReadOnlyList<CloudMass> getMasses() {
    if (masses == null)
      throw new NonsenseRequestException("Unable to get cloud masses for special (VV/NSC/NCD) clouds.");
    return masses;
  }

  public Integer getVerticalVisibilityInHundredFeet() {
    if (verticalVisibility == false)
      throw new NonsenseRequestException("Unable to use this method if no vertical visibility is specified.");
    return verticalVisibilityInHundredFeet;
  }

  public Integer getVerticalVisibilityInFeet(){
    if (this.verticalVisibility == false)
      throw new NonsenseRequestException("Unable to use this method if no vertical visibility is specified.");
    return this.verticalVisibilityInHundredFeet * 100;
  }
  
  public boolean isVerticalVisibility(){
    return this.verticalVisibility;
  }
  
  public boolean isNoSignificant(){
    return this.specialState == CloudInfoSpecialStates.noSignificantClouds;
  }
  
  public boolean isNoDetected(){
    return this.specialState == CloudInfoSpecialStates.noDetectedClouds;
  }
  
  public boolean isSpecialState(){
    return this.specialState != CloudInfoSpecialStates.none;
  }
  
  public boolean isWithMasses(){
    return this.masses != null;
  }
  
  public CloudInfoSpecialStates getSpecialState() {
    return specialState;
  }

  /**
   * Returns true if cloud-info has no state, no vertical visibility and no clouds, so it will not appear in metar report in any style.
   * @return 
   */
  public boolean isEmpty() {
    return
            this.isSpecialState() == false  
            && this.isVerticalVisibility() == false 
            && this.getMasses().isEmpty();
  }
}
