package eng.metarJava.enums;

/**
 * Represents sky cloud coverage as defined by METAR report specification.
 * @author Marek Vajgl
 */
public enum CloudAmount {
  /**
   * Few coverage (1-2/8)
   */
  FEW,
  /**
   * Scattered coverage (3-4/8)
   */
  SCT,
  /**
   * Broken coverage (5-7/8)
   */
  BKN,
  /**
   * Full coverage (8/8)
   */
  OVC  
}
