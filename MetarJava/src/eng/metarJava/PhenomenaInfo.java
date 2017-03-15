package eng.metarJava;

import eng.metarJava.support.*;

/**
 * Represents phenomena description.
 * @author Marek Vajgl
 */
public class PhenomenaInfo {
  private final PhenomenaIntensity intensity;
  private final PhenomenaDescriptor descriptor;
  private final PhenomenaType type;
  private final boolean inVicinity;
  
  public static PhenomenaInfo create(PhenomenaIntensity intensity, PhenomenaDescriptor descriptor, PhenomenaType type, boolean inVicinity){
    PhenomenaInfo ret = new PhenomenaInfo(intensity, descriptor, type, inVicinity);
    return ret;
  }

  protected PhenomenaInfo(PhenomenaIntensity intensity, PhenomenaDescriptor descriptor, PhenomenaType type, boolean inVicinity) {
    this.intensity = intensity;
    this.descriptor = descriptor;
    this.type = type;
    this.inVicinity = inVicinity;
  }
  
  public PhenomenaIntensity getIntensity() {
    return intensity;
  }

  public PhenomenaDescriptor getDescriptor() {
    return descriptor;
  }

  public PhenomenaType getType() {
    return type;
  }

  public boolean isInVicinity() {
    return inVicinity;
  }
}
