package eng.metarJava.support;

import eng.metarJava.enums.SpeedUnit;

/**
 *
 * @author Marek Vajgl
 */
public class Speed {
  private double valueInKmh;
  
  public Speed(double value, SpeedUnit unit){
    if (value < 0)
      throw new IllegalArgumentException("Speed value cannot be less than zero (currently: " + value + ".");
    this.valueInKmh = SpeedUnit.convert(value, unit, SpeedUnit.KMH);
  }
  
  /**
   * Returns speed in unit, rounded to ceiling int.
   * @param unit Unit in which the speed has to be obtained.
   * @return Value of speed.
   */
  public int getIntValue(SpeedUnit unit){
    double pom = getValue(unit);
    int ret = (int) Math.round(pom);
    return ret;
  }
  
  /**
   * returns value in KPH.
   * @return Value in KPH.
   */
  public double getValue(){
    return this.valueInKmh;
  }
  
  public double getValue(SpeedUnit unit){
    double ret = SpeedUnit.convert(this.valueInKmh, SpeedUnit.KMH, unit);
    return ret;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + (int) (Double.doubleToLongBits(this.valueInKmh) ^ (Double.doubleToLongBits(this.valueInKmh) >>> 32));
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Speed other = (Speed) obj;
    if (Double.doubleToLongBits(this.valueInKmh) != Double.doubleToLongBits(other.valueInKmh)) {
      return false;
    }
    return true;
  }
}
