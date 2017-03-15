package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.exception.NonsenseRequestException;
import java.util.List;

/**
 * Represents information aboud clouds in report.
 * @author Marek Vajgl
 */
public class CloudInfo extends TrendCloudInfo {

  /**
   * Creates cloud info with cloud layers.
   * @param cloudMasses Layers of the clouds
   * @return Cloud info object
   */
  public static CloudInfo create(List<CloudMass> cloudMasses){
    CloudInfo ret = new CloudInfo(cloudMasses, null, CloudInfoSpecialStates.none);
    return ret;
  }
  
  /**
   * Creates cloud info with vertical visibility.
   * @param verticalVisibilityInHundredFeet Vertical visibility in hundreds feet
   * @return Cloud info object
   */
  public static CloudInfo createWithVV(int verticalVisibilityInHundredFeet){
    CloudInfo ret = new CloudInfo(null, verticalVisibilityInHundredFeet, CloudInfoSpecialStates.none);
    return ret;
  }
  /**
   * Creates cloud info with NSC (no-significant-cloud) state.
   * @return Cloud info object
   */
  public static CloudInfo createNSC(){
    CloudInfo ret = new CloudInfo(null, null, CloudInfoSpecialStates.NSC);
    return ret;
  }
  /**
   * Creates cloud info with NCD (no-cloud-detected) state.
   * @return Cloud info object
   */
  public static CloudInfo createNCD(){
    CloudInfo ret = new CloudInfo(null, null, CloudInfoSpecialStates.NCD);
    return ret;
  }
  
  protected CloudInfo(List<CloudMass> masses, Integer verticalVisibilityInHundredFeet, CloudInfoSpecialStates specialState) {
    super(masses, verticalVisibilityInHundredFeet, specialState);
  }
  
  public boolean isNCD(){
    return super.getSpecialState() == CloudInfoSpecialStates.NCD;
  }
}
