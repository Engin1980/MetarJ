package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum CloudMassSignificantFlag {
  /***
   * No flag set, means there is not TCU or CB.
   */
  none,
  
  /**
   * Value of /// - means station is not able provide information about CB/TCU.
   */
  undetected,
  
  /**
   * TCU - towering-cumulus.
   */
  TCU,
  
  /**
   * CB - cumulonimbus.
   */
  CB;
  
  /***
   * Converts text into flag value.
   * @param value String value - none, ///, CB or TCU
   * @return 
   */
  public static CloudMassSignificantFlag parse(String value){
    if (value == null || value.isEmpty())
      return none;
    else if (value.equals("///"))
      return undetected;
    else
      return CloudMassSignificantFlag.valueOf(value);
  }
}
