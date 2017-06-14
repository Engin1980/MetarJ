package eng.metarJava.enums;

/**
 *
 * @author Marek Vajgl
 */
public enum DistanceUnit {
  kilometers,
  feet,
  meters,
  miles;
  
  private static final double M_IN_KM = 1000;
  private static final double M_IN_FOOD = 0.3048;
  private static final double M_IN_SM = 1609.35;
  
  public static Double convert(Double value, DistanceUnit sourceUnit, DistanceUnit targetUnit){
    Double ret;
    if (value == null) 
      ret = null;
    else 
      ret = convert((double) value, sourceUnit, targetUnit);
    return ret;
  }
  
  public static double convert(double value, DistanceUnit sourceUnit, DistanceUnit targetUnit){
    double ret;
    if (sourceUnit == targetUnit)
      ret = value;
    else{
      ret = convertToM(value, sourceUnit);
      ret = convertFromM(ret, targetUnit);
    }
    return ret;
  }
  
  private static double convertToM(double value, DistanceUnit sourceUnit){
    double ret;
    switch(sourceUnit){
      case feet:
        ret = value * M_IN_FOOD;
        break;
      case kilometers:
        ret = value * M_IN_KM;
        break;
      case miles:
        ret = value * M_IN_SM;
        break;
      case meters:
        ret = value;
        break;
      default:
        throw new UnsupportedOperationException("Unit " + sourceUnit + " not supported.");
    }
    return ret;
  }
  
  private static double convertFromM(double value, DistanceUnit targetUnit){
    double ret;
    switch(targetUnit){
      case feet:
        ret = value / M_IN_FOOD;
        break;
      case kilometers:
        ret = value / M_IN_KM;
        break;
      case miles:
        ret = value / M_IN_SM;
        break;
      case meters:
        ret = value;
        break;
      default:
        throw new UnsupportedOperationException("Unit " + targetUnit + " not supported.");
    }
    return ret;    
  }
}
