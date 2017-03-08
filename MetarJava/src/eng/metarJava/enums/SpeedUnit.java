package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum SpeedUnit {
  KMH,
  KT,
  MPS;
  
  private static final double TO_KT = 1.852;
  private static final double TO_MPS = 3.6;
  
  public static double convert(int value, SpeedUnit sourceUnit, SpeedUnit targetUnit){
    Double inKmh = convertToKmh((double)value, sourceUnit);
    Double ret = convertFromKmh(inKmh, targetUnit);
    return ret;
  }
  
  public static Double convert(Integer value, SpeedUnit sourceUnit, SpeedUnit targetUnit){
    if (value == null) return null;
    Double ret = convert((double) value, sourceUnit, targetUnit);
    return ret;
  }
  
  public static Double convert(Double value, SpeedUnit sourceUnit, SpeedUnit targetUnit){
    if (value == null) return null;
    Double inKmh = convertToKmh(value, sourceUnit);
    Double ret = convertFromKmh(inKmh, targetUnit);
    return ret;
  }
  
  private static Double convertToKmh(Double value, SpeedUnit unit){
    if (value == null) return null;
    switch (unit){
      case KMH:
        return value;
      case KT:
        return value * TO_KT;
      case  MPS:
        return value * TO_MPS;
      default:
        throw new UnsupportedOperationException("Unknown enum type of [SpeedUnit].");
    }
  }
  private static Double convertFromKmh(Double value, SpeedUnit unit){
    if (value == null) return null;
    switch (unit){
      case KMH:
        return value;
      case KT:
        return value / TO_KT;
      case  MPS:
        return value / TO_MPS;
      default:
        throw new UnsupportedOperationException("Unknown enum type of [SpeedUnit].");
    }
  }
}
