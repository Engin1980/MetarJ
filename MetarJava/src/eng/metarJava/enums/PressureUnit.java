package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum PressureUnit {
  hpa,
  inHq;
  
  private static final double InHqToHPa = 1013d/29.92d; // 33.8638866667183;
  
  
  public static double convert(double value, PressureUnit sourceUnit, PressureUnit targetUnit){
    double ret;
    if (sourceUnit == targetUnit)
      ret = value;
    else{
      switch (sourceUnit){
        case hpa:
          ret = value / InHqToHPa;
          break;
        case inHq:
          ret = value * InHqToHPa;
          break;
        default:
          throw new UnsupportedOperationException("This is not implemented.");
      }
    }
    return ret;
  }
}
