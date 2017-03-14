package eng.metarJava;

import eng.metarJava.enums.CloudInfoSpecialStates;
import eng.metarJava.exception.NonsenseRequestException;
import eng.metarJava.support.CloudMass;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class CloudInfo extends TrendCloudInfo {

  public static CloudInfo create(List<CloudMass> cloudMasses){
    CloudInfo ret = new CloudInfo(cloudMasses, null, CloudInfoSpecialStates.none);
    return ret;
  }
  public static CloudInfo createWithVV(int verticalVisibilityInHundredFeet){
    CloudInfo ret = new CloudInfo(null, verticalVisibilityInHundredFeet, CloudInfoSpecialStates.none);
    return ret;
  }
  public static CloudInfo createNSC(){
    CloudInfo ret = new CloudInfo(null, null, CloudInfoSpecialStates.NSC);
    return ret;
  }
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
