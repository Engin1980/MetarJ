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
   * No significant clouds.
   */
  NSC,
  /**
   * No clouds detected or CLR for US metars.
   */
  NCD
}
