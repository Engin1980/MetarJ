package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.exception.NonsenseRequestException;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendCloudInfo {
  private final List<CloudMass> masses;
  private final Integer verticalVisibilityInHundredFeet;
  private final boolean verticalVisibility;
  private final CloudInfoSpecialStates specialState;

  public static TrendCloudInfo create(List<CloudMass> cloudMasses){
    TrendCloudInfo ret = new TrendCloudInfo(cloudMasses, false, null, CloudInfoSpecialStates.none);
    return ret;
  }
  
  public static TrendCloudInfo createWithVV(int verticalVisibilityInHundredFeet){
    TrendCloudInfo ret = new TrendCloudInfo(null, true, verticalVisibilityInHundredFeet, CloudInfoSpecialStates.none);
    return ret;
  }
  public static TrendCloudInfo createNSC(){
    TrendCloudInfo ret = new TrendCloudInfo(null, false, null, CloudInfoSpecialStates.NSC);
    return ret;
  }
  

  protected TrendCloudInfo(List<CloudMass> masses, boolean isVerticalVisibility, Integer verticalVisibilityInHundredFeet, CloudInfoSpecialStates specialState) {
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

  public List<CloudMass> getMasses() {
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
  
  public boolean isNSC(){
    return this.specialState == CloudInfoSpecialStates.NSC;
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
}
