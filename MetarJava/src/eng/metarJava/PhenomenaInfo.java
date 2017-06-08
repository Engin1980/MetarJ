package eng.metarJava;

import eng.metarJava.enums.PhenomenaType;
import eng.metarJava.enums.PhenomenaIntensity;
import eng.metarJava.exception.NullArgumentException;
import eng.metarJava.support.*;
import java.util.Arrays;
import java.util.List;

/**
 * Represents phenomena description.
 * @author Marek Vajgl
 */
public class PhenomenaInfo {
  private final PhenomenaIntensity intensity;
  private final boolean inVicinity;
  private final PhenomenaType[] types;
  
  public static PhenomenaInfo create(PhenomenaIntensity intensity, PhenomenaType[] types, boolean isInVicinity){
    PhenomenaInfo ret = new PhenomenaInfo(intensity, types, isInVicinity);
    return ret;
  }
  public static PhenomenaInfo create(PhenomenaIntensity intensity, List<PhenomenaType> types, boolean isInVicinity){
    PhenomenaType[] arr = types.toArray(new PhenomenaType[0]);
    PhenomenaInfo ret = new PhenomenaInfo(intensity, arr, isInVicinity);
    return ret;
  }
  public static PhenomenaInfo create(PhenomenaIntensity intensity, PhenomenaType type, boolean isInVicinity){
    PhenomenaInfo ret = new PhenomenaInfo(intensity, new PhenomenaType[]{type}, isInVicinity);
    return ret;
  }
  public static PhenomenaInfo createEmpty(){
    PhenomenaInfo ret = new PhenomenaInfo(PhenomenaIntensity.moderate, new PhenomenaType[0], false);
    return ret;
  }

  protected PhenomenaInfo(PhenomenaIntensity intensity, PhenomenaType[] types, boolean isInVicinity) {
    this.intensity = intensity;
    
    if (types == null)
      throw new NullArgumentException("PhenomenaType type array cannot be null. Use empty array if required.");
    if (types.length > 2)
      throw new IllegalArgumentException("PhenomenaType[] type array must have at most two elements (currently " + types.length + ".");
    if (types.length == 0 && isInVicinity)
      throw new IllegalArgumentException("PhenomenaType [] types cannot be empty, if \"isInVicinity\" flag is set to true.");
    if (types.length == 0 && intensity != PhenomenaIntensity.moderate)
      throw new IllegalArgumentException("PhenomenaType [] types cannot be empty, if \"intensity\" flag is different from moderate.");
    
    this.types = Arrays.copyOf(types, types.length);
    this.inVicinity = isInVicinity;
  }
  
  public PhenomenaIntensity getIntensity() {
    return intensity;
  }

  public PhenomenaType[] getTypes() {
    PhenomenaType[] ret = Arrays.copyOf(this.types, this.types.length);
    return ret;
  }

  public boolean isInVicinity() {
    return inVicinity;
  }
}
