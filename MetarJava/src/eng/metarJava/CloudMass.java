package eng.metarJava;

import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.CloudMassSignificantFlag;

/**
 * Represents information about one cloud layer.
 * @author Marek Vajgl
 */
public class CloudMass {
  private final CloudAmount amount;
  private final int baseHeightHundredFeet;
  private final CloudMassSignificantFlag significantFlag;

  /**
   * Creates cloud mass layer with CB (cumulonimbus) cloud flag.
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass createCB(CloudAmount amount, int baseHeightHundredFeet){
    CloudMass ret = create(amount, baseHeightHundredFeet, CloudMassSignificantFlag.CB);
    return ret;
  }
  /**
   * Creates cloud mass layer with TCU (towering-cumulus) cloud flag.
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass createTCU(CloudAmount amount, int baseHeightHundredFeet){
    CloudMass ret = create(amount, baseHeightHundredFeet, CloudMassSignificantFlag.TCU);
    return ret;
  }
  /**
   * Creates cloud mass layer with optional significant flag.
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @param significantFlag Flag of cloud level significancy.
   * @return New instance of CloudMass
   */  
  public static CloudMass create(CloudAmount amount, int baseHeightHundredFeet, CloudMassSignificantFlag significantFlag){
    CloudMass ret = new CloudMass(amount, baseHeightHundredFeet, significantFlag);
    return ret;
  }
  /**
   * Creates simple cloud mass layer.
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass create(CloudAmount amount, int baseHeightHundredFeet){
    CloudMass ret = new CloudMass(amount, baseHeightHundredFeet, CloudMassSignificantFlag.none);
    return ret;
  }
  
  protected CloudMass(CloudAmount amount, int baseHeightHundredFeet, CloudMassSignificantFlag significantFlag) {
    if (baseHeightHundredFeet < 0)
      throw new IllegalArgumentException("[baseHeightHundredFeet] should not be negative.");
    this.amount = amount;
    this.baseHeightHundredFeet = baseHeightHundredFeet;
    this.significantFlag = significantFlag;
  }

  /**
   * Returns sky cloud coverage.
   * @return 
   */
  public CloudAmount getAmount() {
    return amount;
  }

  /**
   * Returns sky cloud base level in hundreds feet.
   * @return Hundreds feet value.
   */
  public int getBaseHeightHundredFeet() {
    return baseHeightHundredFeet;
  }
  
  /**
   * Return sky cloud base level in feet.
   * @return Sky level cloud in feet.
   */
  public int getBaseHeightFeet(){
    return this.baseHeightHundredFeet*100;
  }

  /**
   * Returns value of cloud level special flag (NSC/NCD).
   * @return 
   */
  public CloudMassSignificantFlag getSignificantFlag() {
    return significantFlag;
  }
  
}
