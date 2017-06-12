package eng.metarJava.support;

import eng.objectTreeBuilder.attributes.DisplayValueFromString;

/**
 * Represents heading in degrees in range 0 - 359. Exceeding values will be aligned into this range.
 * @author Marek Vajgl
 */
@DisplayValueFromString
public class Heading {

  private final int value;

  public Heading(int value) {
    if (value < 0) {
      value = (int) Math.abs(value);
      value = value % 360;
      value = 360 - value;
    } else if (value > 360) {
      value = value % 360;
    }
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + this.value;
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
    final Heading other = (Heading) obj;
    if (this.value != other.value) {
      return false;
    }
    return true;
  }

  
}
