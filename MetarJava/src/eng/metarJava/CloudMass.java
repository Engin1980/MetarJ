package eng.metarJava;

import eng.metarJava.enums.CloudAmount;
import eng.metarJava.enums.CloudMassSignificantFlag;
import eng.metarJava.exception.NonsenseRequestException;
import java.util.Optional;

/**
 * Represents information about one cloud layer.
 *
 * @author Marek Vajgl
 */
public class CloudMass {

  private final CloudAmount amount;
  private final Integer baseHeightHundredFeet;
  private final CloudMassSignificantFlag significantFlag;

  /**
   * Creates cloud mass layer without known coverage and base level, but with significancy flag.
   *
   * @param flag
   * @return
   */
  public static CloudMass createWithoutAmountAndBaseHeight(CloudMassSignificantFlag flag) {
    CloudMass ret = new CloudMass(null, null, flag);
    return ret;
  }

  /**
   * Creates cloud mass layer without known coverage and base level, but with TCU (towering-cumulus). Typically reported by automatic systems.
   *
   * @return
   */
  public static CloudMass createWithoutAmountAndBaseHeightTCU() {
    return createWithoutAmountAndBaseHeight(CloudMassSignificantFlag.TCU);
  }

  /**
   * Creates cloud mass layer without known coverage and base level, but with CB (cumulonimbus). Typically reported by automatic systems.
   *
   * @return
   */
  public static CloudMass createWithoutAmountAndBaseHeightCB() {
    return createWithoutAmountAndBaseHeight(CloudMassSignificantFlag.CB);
  }

  /**
   * Creates cloud mass layer with CB (cumulonimbus) cloud flag.
   *
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass createCB(CloudAmount amount, int baseHeightHundredFeet) {
    CloudMass ret = create(amount, baseHeightHundredFeet, CloudMassSignificantFlag.CB);
    return ret;
  }

  /**
   * Creates cloud mass layer with TCU (towering-cumulus) cloud flag.
   *
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass createTCU(CloudAmount amount, int baseHeightHundredFeet) {
    CloudMass ret = create(amount, baseHeightHundredFeet, CloudMassSignificantFlag.TCU);
    return ret;
  }

  /**
   * Creates cloud mass layer with optional significant flag.
   *
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @param significantFlag Flag of cloud level significancy.
   * @return New instance of CloudMass
   */
  public static CloudMass create(CloudAmount amount, int baseHeightHundredFeet, CloudMassSignificantFlag significantFlag) {
    CloudMass ret = new CloudMass(amount, baseHeightHundredFeet, significantFlag);
    return ret;
  }

  /**
   * Creates simple cloud mass layer.
   *
   * @param amount Amount of the coverage.
   * @param baseHeightHundredFeet Base cloud level in hundreds feet.
   * @return New instance of CloudMass
   */
  public static CloudMass create(CloudAmount amount, int baseHeightHundredFeet) {
    CloudMass ret = new CloudMass(amount, baseHeightHundredFeet, CloudMassSignificantFlag.none);
    return ret;
  }
  
  /**
   * Creates simple cloud mass layer.
   * @param cloudAmountOrNull Cloud amount, or null if it is not known.
   * @param baseHeightInHundredFeetOrNull Cloud base height, or null if it is not known.
   * @param flagOrNull Flag, or null if there is no flag. Null will be set to  [none] enum value.
   * @return 
   * There only following valid combinations of parameters:
   * - if amount and height are unknown (typical for automated systems), flag must be set to CB/TCU.
   * - if amount is known, height must be set too and vice versa. Then, flag CB/TCU or [undetected] [///] can be set. 
   */
  public static CloudMass createWithOptionals(
          CloudAmount cloudAmountOrNull,
          Integer baseHeightInHundredFeetOrNull,
          CloudMassSignificantFlag flagOrNull){
    
   CloudMass ret = new CloudMass(cloudAmountOrNull, baseHeightInHundredFeetOrNull, flagOrNull);
   return ret;
  }

  protected CloudMass(CloudAmount amount, Integer baseHeight, CloudMassSignificantFlag significantFlag) {
    if ((amount == null && baseHeight != null) 
            || (amount != null && baseHeight == null)){
      throw new IllegalArgumentException("[amount] and [baseHeight] both must be set or both must be null.");
    }
    if (baseHeight != null && baseHeight < 0) {
      throw new IllegalArgumentException("[baseHeight] should not be negative.");
    }
    if (amount == null && significantFlag == CloudMassSignificantFlag.none){
      throw new IllegalArgumentException("[significantFlag] must be set to CB/TCU if [amoun] is null.");
    }
    this.amount = amount;
    this.baseHeightHundredFeet = baseHeight;
    this.significantFlag = significantFlag;
  }

  /**
   * Returns if amount and base height are known, false otherwise.
   * @return 
   */
  public boolean isAmountAndBaseHeightKnown(){
    return amount != null;
  }
  
  /**
   * Returns sky cloud coverage.
   *
   * @return
   */
  public CloudAmount getAmount() {
    if (isAmountAndBaseHeightKnown() == false)
      throw new NonsenseRequestException("Unable to get this value if [isAmountAndBaseHeightKnown()] is false.");
    return amount;
  }

  /**
   * Returns sky cloud base level in hundreds feet.
   *
   * @return Hundreds feet value.
   */
  public int getBaseHeightHundredFeet() {
    if (isAmountAndBaseHeightKnown() == false)
      throw new NonsenseRequestException("Unable to get this value if [isAmountAndBaseHeightKnown()] is false.");
    return baseHeightHundredFeet;
  }

  /**
   * Return sky cloud base level in feet.
   *
   * @return Sky level cloud in feet.
   */
  public int getBaseHeightFeet() {
    return this.baseHeightHundredFeet * 100;
  }

  /**
   * Returns value of cloud level special flag (NSC/NCD).
   *
   * @return
   */
  public CloudMassSignificantFlag getSignificantFlag() {
    return significantFlag;
  }

}
