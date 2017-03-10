package eng.metarJava.support;

import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.CloudMassSignificantFlag;

/**
 *
 * @author Marek Vajgl
 */
public class CloudMass {
  private final CloudAmount amount;
  private final int baseHeightHundredFeet;
  private final CloudMassSignificantFlag significantFlag;

  public CloudMass(CloudAmount amount, int baseHeightHundredFeet, CloudMassSignificantFlag significantFlag) {
    if (baseHeightHundredFeet < 0)
      throw new IllegalArgumentException("[baseHeightHundredFeet] should not be negative.");
    this.amount = amount;
    this.baseHeightHundredFeet = baseHeightHundredFeet;
    this.significantFlag = significantFlag;
  }

  public CloudAmount getAmount() {
    return amount;
  }

  public int getBaseHeightHundredFeet() {
    return baseHeightHundredFeet;
  }
  
  public int getBaseHeightFeet(){
    return this.baseHeightHundredFeet*100;
  }

  public CloudMassSignificantFlag getSignificantFlag() {
    return significantFlag;
  }
  
}
