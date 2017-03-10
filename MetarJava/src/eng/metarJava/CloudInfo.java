package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.support.CloudMass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class CloudInfo {

  private final List<CloudMass> masses;
  private final Integer verticalVisibilityInHundredFeet;
  private final CloudInfoSpecialStates specialState;

  /**
   * Creates Cloud info with singificant cloud layers reported.
   *
   * @param cloudMass
   */
  public CloudInfo(CloudMass... cloudMass) {
    this.masses = Arrays.asList(cloudMass);
    this.verticalVisibilityInHundredFeet = null;
    this.specialState = CloudInfoSpecialStates.none;
  }

  /**
   * Creates Cloud info with singificant cloud layers reported.
   *
   * @param cloudMasses
   */
  public CloudInfo(List<CloudMass> cloudMasses) {
    this.masses = cloudMasses;
    this.verticalVisibilityInHundredFeet = null;
    this.specialState = CloudInfoSpecialStates.none;
  }

  /**
   * Creates Cloud info as Vertical Visibility value
   *
   * @param verticalVisiblityInHundredFeet
   */
  public CloudInfo(int verticalVisiblityInHundredFeet) {
    this.masses = null;
    this.verticalVisibilityInHundredFeet = verticalVisiblityInHundredFeet;
    this.specialState = CloudInfoSpecialStates.none;
  }

  /**
   * Creates Cloud info with special state
   *
   * @param state
   */
  public CloudInfo(CloudInfoSpecialStates state) {
    if (state == CloudInfoSpecialStates.none) {
      throw new IllegalArgumentException("[state] with value [none] is not valid. Either significant state must be specified (NCD/NSC) or cloud mass or vertical visibility must be specified (different constructors).");
    }
    this.masses = null;
    this.verticalVisibilityInHundredFeet = null;
    this.specialState = state;
  }

  public List<CloudMass> getMasses() {
    return masses;
  }

  public Integer getVerticalVisibilityInHundredFeet() {
    return verticalVisibilityInHundredFeet;
  }

  public int getVerticalVisibilityInFeet(){
    if (this.isVerticalVisibility() == false)
      throw new UnsupportedOperationException("Unable to use this method if no vertical visibility is specified.");
    return this.verticalVisibilityInHundredFeet * 100;
  }
  
  public boolean isVerticalVisibility(){
    return this.verticalVisibilityInHundredFeet != null;
  }
  
  public boolean isNSC(){
    return this.specialState == CloudInfoSpecialStates.NSC;
  }
  
  public boolean isNCD(){
    return this.specialState == CloudInfoSpecialStates.NCD;
  }
  
  public boolean isSpecialState(){
    return this.specialState != CloudInfoSpecialStates.none;
  }
  
  public CloudInfoSpecialStates getSpecialState() {
    return specialState;
  }
}
