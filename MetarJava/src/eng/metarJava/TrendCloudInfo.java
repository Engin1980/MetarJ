package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.support.CloudMass;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TrendCloudInfo {
  private final List<CloudMass> masses;
  private final Integer verticalVisibilityInHundredFeet;
  private final CloudInfoSpecialStates specialState;

  public static TrendCloudInfo create(List<CloudMass> cloudMasses){
    TrendCloudInfo ret = new TrendCloudInfo(cloudMasses, null, CloudInfoSpecialStates.none);
    return ret;
  }
  public static TrendCloudInfo createWithVV(int verticalVisibilityInHundredFeet){
    TrendCloudInfo ret = new TrendCloudInfo(null, verticalVisibilityInHundredFeet, CloudInfoSpecialStates.none);
    return ret;
  }
  public static TrendCloudInfo createNSC(){
    TrendCloudInfo ret = new TrendCloudInfo(null, null, CloudInfoSpecialStates.NSC);
    return ret;
  }
  

  protected TrendCloudInfo(List<CloudMass> masses, Integer verticalVisibilityInHundredFeet, CloudInfoSpecialStates specialState) {
    this.masses = masses;
    this.verticalVisibilityInHundredFeet = verticalVisibilityInHundredFeet;
    this.specialState = specialState;
  }

  public List<CloudMass> getMasses() {
    if (masses == null)
      throw new NonsenseRequestException("Unable to get cloud masses for special (VV/NSC/NCD) clouds.");
    return masses;
  }

  public int getVerticalVisibilityInHundredFeet() {
    if (isVerticalVisibility() == false)
      throw new NonsenseRequestException("Unable to use this method if no vertical visibility is specified.");
    return verticalVisibilityInHundredFeet;
  }

  public int getVerticalVisibilityInFeet(){
    if (this.isVerticalVisibility() == false)
      throw new NonsenseRequestException("Unable to use this method if no vertical visibility is specified.");
    return this.verticalVisibilityInHundredFeet * 100;
  }
  
  public boolean isVerticalVisibility(){
    return this.verticalVisibilityInHundredFeet != null;
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
