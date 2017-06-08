package eng.metarJava.enums;

/**
 * Represents intensity of phenomena. Used for {@linkplain eng.metarJava.PhenomenaInfo}. Remark: In vicinity - VC - is no an intensity
 * value. It is set as a flag in {@linkplain  eng.metarJava.PhenomenaInfo} object.
 *
 * @author Marek Vajgl
 * @see eng.metarJava.PhenomenaInfo
 * @see PhenomenaType
 *
 */
public enum PhenomenaIntensity {
  /**
   * Light intensity, represented by minus (-) char
   */
  light,
  /**
   * Moderate intensity, without prefix in report text
   */
  moderate,
  /**
   * Heavy intensity, represented by plus (+) char
   */
  heavy
}
