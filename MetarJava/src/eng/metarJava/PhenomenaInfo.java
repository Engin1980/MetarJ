package eng.metarJava;

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
  private final PhenomenaType[] types;
  
  public static PhenomenaInfo create(PhenomenaIntensity intensity, PhenomenaType[] types){
    PhenomenaInfo ret = new PhenomenaInfo(intensity, types);
    return ret;
  }
  public static PhenomenaInfo create(PhenomenaIntensity intensity, List<PhenomenaType> types){
    PhenomenaType[] arr = types.toArray(new PhenomenaType[0]);
    PhenomenaInfo ret = new PhenomenaInfo(intensity, arr);
    return ret;
  }
  public static PhenomenaInfo create(PhenomenaIntensity intensity, PhenomenaType type){
    PhenomenaInfo ret = new PhenomenaInfo(intensity, new PhenomenaType[]{type});
    return ret;
  }
  public static PhenomenaInfo createEmpty(){
    PhenomenaInfo ret = new PhenomenaInfo(PhenomenaIntensity.moderate, new PhenomenaType[0]);
    return ret;
  }

  protected PhenomenaInfo(PhenomenaIntensity intensity, PhenomenaType[] types) {
    this.intensity = intensity;
    
    if (types == null)
      throw new NullArgumentException("PhenomenaType type array cannot be null. Use empty array if required.");
    if (types.length > 2)
      throw new IllegalArgumentException("PhenomenaType[] type array must have at most two elements (currently " + types.length + ".");
    this.types = Arrays.copyOf(types, types.length);
  }
  
  public PhenomenaIntensity getIntensity() {
    return intensity;
  }

  public PhenomenaType[] getTypes() {
    PhenomenaType[] ret = Arrays.copyOf(this.types, this.types.length);
    return ret;
  }
}
