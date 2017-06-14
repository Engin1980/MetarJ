package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum PressureUnit {
  hpa,
  inHq;
  
  private static final double hpaToInHg = 0.0590599661428;
  
  public static double convert(double value, PressureUnit sourceUnit, PressureUnit targetUnit){
    double ret;
    if (sourceUnit == targetUnit)
      ret = value;
    else{
      switch (sourceUnit){
        case hpa:
          ret = value * hpaToInHg;
          break;
        case inHq:
          ret = value / hpaToInHg;
          break;
        default:
          throw new UnsupportedOperationException("This is not implemented.");
      }
    }
    return ret;
  }
}
