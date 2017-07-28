package eng.metarJava.enums;

/**
 * Defines cloud special states. See {@linkplain eng.metarJava.CloudInfo} or {@linkplain  eng.metarJava.TrendCloudInfo}.
 * @author Marek Vajgl
 */
public enum CloudInfoSpecialStates {
  /**
   * No special state.
   */
  none,
  /**
   * No significant clouds. Used when none or no significant clouds are present over area.
   */
  noSignificantClouds,
  /**
   * No clouds detected. Used when no clouds are detected by automated stations.
   */
  noDetectedClouds
}
